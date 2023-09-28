package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.BookedPropertiesStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.BookedPropertyStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookedPropertiesStatisticsUseCaseImpl implements BookedPropertiesStatisticsUseCase {
    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private UserRepository userRepository;
    private AccessToken accessToken;

    @Override
    public List<BookedPropertyStatisticsDTO> getBookedPropertiesStatistics(long hostId) {
        if (accessToken.getUserId() != hostId)
            throw new UnauthorizedDataException();

        Optional<UserEntity> host = userRepository.findById(hostId);
        if (host.isEmpty())
            throw new UserNotFoundException();

        List<PropertyEntity> propertyEntities = propertyRepository.findByUser(host.get());
        List<BookingEntity> bookings = new ArrayList<>();

        for (PropertyEntity property : propertyEntities) {
            bookings.addAll(bookingRepository.findByPropertyId(property));
        }
        List<BookedPropertyStatisticsDTO> statistics = propertyEntities.stream()
                .map(property -> {
                    long bookingCount = bookings.stream()
                            .filter(booking -> booking.getPropertyId().getPropertyId() == property.getPropertyId())
                            .count();

                    return BookedPropertyStatisticsDTO.builder()
                            .propertyName(property.getName())
                            .bookingCount(bookingCount)
                            .build();
                })
                .sorted(Comparator.comparingLong(BookedPropertyStatisticsDTO::getBookingCount).reversed())
                .limit(10)
                .toList();

        return statistics;
    }
}
