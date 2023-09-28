package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookedDatesForPropertyUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private GetBookedDatesForPropertyUseCaseImpl getBookedDatesForPropertyUseCase;

    @Test
    void getBookedDates() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-12T00:00:00Z");

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setPropertyId(property);
        booking.setUserId(new UserEntity());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setPrice(100);

        BookingEntity booking2 = new BookingEntity();
        booking2.setBookingId(2);
        booking2.setPropertyId(property2);

        when(bookingRepository.findAll()).thenReturn(List.of(booking, booking2));

        List<Instant> bookedDates = getBookedDatesForPropertyUseCase.getBookedDates(property.getPropertyId());

        assertEquals(startDate, bookedDates.get(0));
        verify(bookingRepository).findAll();
    }
}