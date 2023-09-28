package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookingsForUserUseCase;
import getawaygo_project.getawaygo_backend.business.exception.NoBookingsFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.Booking;
import getawaygo_project.getawaygo_backend.domain.GetAllBookingsResponse;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBookingsForUserUseCaseImpl implements GetBookingsForUserUseCase {
    private BookingRepository bookingRepository;
    private AccessToken accessToken;

    @Override
    public GetAllBookingsResponse getBookingsForUser(long userId) {
        if (accessToken.getUserId() != userId)
            throw new UnauthorizedDataException();
        List<BookingEntity> bookings = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getUserId().getUserId() == userId)
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
