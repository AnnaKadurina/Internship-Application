package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoBookingsFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllBookingsResponse;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookingsForPropertyUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private GetBookingsForPropertyUseCaseImpl getBookingsForPropertyUseCase;

    @Test
    void getBookingsForProperty() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        PropertyEntity property2 = new PropertyEntity();
        property.setPropertyId(2);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setUserId(new UserEntity());
        booking.setPropertyId(property);

        BookingEntity booking2 = new BookingEntity();
        booking2.setBookingId(2);
        booking2.setUserId(new UserEntity());
        booking2.setPropertyId(property2);

        List<BookingEntity> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);

        when(bookingRepository.findAll()).thenReturn(bookings);

        GetAllBookingsResponse response = getBookingsForPropertyUseCase.getBookingsForProperty(property.getPropertyId());

        assertEquals(1, response.getAllBookings().size());

        verify(bookingRepository).findAll();

    }

    @Test
    void getBookingsForPropertyNotFound() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setUserId(new UserEntity());
        booking.setPropertyId(property);

        List<BookingEntity> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingRepository.findAll()).thenReturn(bookings);

        assertThrows(NoBookingsFoundException.class, () -> getBookingsForPropertyUseCase.getBookingsForProperty(5));

        verify(bookingRepository).findAll();

    }
}