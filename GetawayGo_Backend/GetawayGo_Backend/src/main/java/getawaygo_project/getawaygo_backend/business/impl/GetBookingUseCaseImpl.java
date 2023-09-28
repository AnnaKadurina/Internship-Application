package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookingUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.Booking;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetBookingUseCaseImpl implements GetBookingUseCase {
    private BookingRepository bookingRepository;
    private AccessToken accessToken;

    @Override
    public Optional<Booking> getBooking(long id) {
        Optional<Booking> findBooking = bookingRepository.findById(id).map(BookingConverter::convert);
        if (findBooking.isEmpty())
            throw new BookingNotFoundException();
        else {
            if (findBooking.get().getUserId() != accessToken.getUserId())
                throw new UnauthorizedDataException();
            else
                return findBooking;
        }
    }
}
