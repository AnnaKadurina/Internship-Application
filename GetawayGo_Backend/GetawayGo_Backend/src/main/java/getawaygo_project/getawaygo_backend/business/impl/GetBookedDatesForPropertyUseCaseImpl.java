package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookedDatesForPropertyUseCase;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GetBookedDatesForPropertyUseCaseImpl implements GetBookedDatesForPropertyUseCase {
    private BookingRepository bookingRepository;

    @Override
    public List<Instant> getBookedDates(long propertyId) {
        List<BookingEntity> bookings = bookingRepository.findAll().stream().filter(b -> b.getPropertyId().getPropertyId() == propertyId).toList();

        List<Instant> bookedDates = new ArrayList<>();

        for (BookingEntity booking : bookings) {
            Instant startDate = booking.getStartDate();
            Instant endDate = booking.getEndDate();

            LocalDateTime startDateTime = LocalDateTime.ofInstant(startDate, ZoneId.of("UTC"));
            LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate, ZoneId.of("UTC"));

            LocalDateTime dateTime = startDateTime;
            while (!dateTime.isAfter(endDateTime)) {
                bookedDates.add(dateTime.toInstant(ZoneOffset.UTC));
                dateTime = dateTime.plusDays(1);
            }
        }
        return bookedDates;
    }
}
