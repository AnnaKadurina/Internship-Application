package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.DeleteBookingUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteBookingUseCaseImpl implements DeleteBookingUseCase {
    private BookingRepository bookingRepository;
    @Override
    public void deleteBooking(long bookingId) {
        if(bookingRepository.existsById(bookingId))
            this.bookingRepository.deleteById(bookingId);
        else
            throw new BookingNotFoundException();

    }
}
