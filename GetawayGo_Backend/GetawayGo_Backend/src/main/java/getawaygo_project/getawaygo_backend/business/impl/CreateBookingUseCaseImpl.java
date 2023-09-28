package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.CreateBookingUseCase;
import getawaygo_project.getawaygo_backend.business.GetBookedDatesForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookedDatesException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.CreateBookingRequest;
import getawaygo_project.getawaygo_backend.domain.CreateBookingResponse;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateBookingUseCaseImpl implements CreateBookingUseCase {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private PropertyRepository propertyRepository;
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;

    @Override
    public CreateBookingResponse createBooking(CreateBookingRequest bookingRequest) {
        Optional<UserEntity> user = userRepository.findById(bookingRequest.getUserId());
        if (user.isEmpty())
            throw new UserNotFoundException();

        Optional<PropertyEntity> property = propertyRepository.findById(bookingRequest.getPropertyId());
        if (property.isEmpty())
            throw new PropertyIsNotFoundException();

        Instant startDate = bookingRequest.getStartDate();
        Instant endDate = bookingRequest.getEndDate();

        if (endDate.isBefore(startDate))
            throw new BookedDatesException();

        List<Instant> bookedDates = getBookedDatesForPropertyUseCase.getBookedDates(bookingRequest.getPropertyId());

        for (Instant bookedDate : bookedDates) {
            if (bookedDate.isAfter(startDate) && bookedDate.isBefore(endDate)) {
                throw new BookedDatesException();
            }
        }

        long totalNights = Duration.between(startDate, endDate).toDays();
        double totalPrice = totalNights * property.get().getPrice();

        BookingEntity booking = new BookingEntity();
        booking.setUserId(user.get());
        booking.setPropertyId(property.get());
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        booking.setPrice(totalPrice);

        bookingRepository.save(booking);
        return CreateBookingResponse.builder()
                .bookingResponse(BookingConverter.convert(booking))
                .build();

    }
}
