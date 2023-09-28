package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookingsForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.NoBookingsFoundException;
import getawaygo_project.getawaygo_backend.domain.Booking;
import getawaygo_project.getawaygo_backend.domain.GetAllBookingsResponse;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBookingsForPropertyUseCaseImpl implements GetBookingsForPropertyUseCase {
    private BookingRepository bookingRepository;
    @Override
    public GetAllBookingsResponse getBookingsForProperty(long propertyId) {
        List<BookingEntity> bookings = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getPropertyId().getPropertyId() == propertyId)
                .toList();
        final GetAllBookingsResponse response = new GetAllBookingsResponse();
        List<Booking> results = bookings.stream()
                .map(BookingConverter::convert)
                .toList();
        if (results.isEmpty())
            throw new NoBookingsFoundException();
        else {
            response.setAllBookings(results);
            return response;
        }
    }
}
