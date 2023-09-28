package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookedDatesForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.UpdateBookingUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookedDatesException;
import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.domain.UpdateBookingRequest;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateBookingUseCaseImpl implements UpdateBookingUseCase {
    private final BookingRepository bookingRepository;
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;

    @Override
    public void updateBooking(UpdateBookingRequest updateBookingRequest) {
        Optional<BookingEntity> bookingUpdate = bookingRepository.findById(updateBookingRequest.getBookingId());
        if (bookingUpdate.isEmpty())
            throw new BookingNotFoundException();

        Instant startDate = updateBookingRequest.getStartDate();
        Instant endDate = updateBookingRequest.getEndDate();

        if (endDate.isBefore(startDate))
            throw new BookedDatesException();

        List<Instant> bookedDates = getBookedDatesForPropertyUseCase.getBookedDates(bookingUpdate.get().getPropertyId().getPropertyId());

        bookedDates.removeIf(date -> date.equals(bookingUpdate.get().getStartDate()) || date.equals(bookingUpdate.get().getEndDate()));

        for (Instant bookedDate : bookedDates) {
            if (bookedDate.isAfter(startDate) && bookedDate.isBefore(endDate)) {
                throw new BookedDatesException();
            }
        }

        long totalNights = Duration.between(startDate, endDate).toDays();
        double totalPrice = totalNights * bookingUpdate.get().getPropertyId().getPrice();

        BookingEntity booking = bookingUpdate.get();
        booking.setStartDate(updateBookingRequest.getStartDate());
        booking.setEndDate(updateBookingRequest.getEndDate());
        booking.setPrice(totalPrice);
        bookingRepository.save(booking);
    }
}
