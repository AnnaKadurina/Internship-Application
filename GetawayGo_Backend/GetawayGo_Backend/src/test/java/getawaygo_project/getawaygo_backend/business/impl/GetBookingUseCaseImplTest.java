package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.Booking;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookingUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private GetBookingUseCaseImpl getBookingUseCase;

    @Test
    void getBooking() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setUserId(user);
        booking.setPropertyId(property);

        when(bookingRepository.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(accessToken.getUserId()).thenReturn(user.getUserId());

        Optional<Booking> getBooking = getBookingUseCase.getBooking(booking.getBookingId());

        assertEquals(booking.getBookingId(), getBooking.get().getBookingId());
        verify(bookingRepository).findById(booking.getBookingId());
        verify(accessToken).getUserId();
    }

    @Test
    void getBookingNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> getBookingUseCase.getBooking(bookingId));

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getBookingUnauthorized() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setUserId(user);
        booking.setPropertyId(property);

        Long bookingId = booking.getBookingId();

        when(bookingRepository.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(accessToken.getUserId()).thenReturn(5L);

        assertThrows(UnauthorizedDataException.class, () -> getBookingUseCase.getBooking(bookingId));

        verify(bookingRepository).findById(booking.getBookingId());
        verify(accessToken).getUserId();
    }
}