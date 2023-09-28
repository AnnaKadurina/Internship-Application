package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoBookingsFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
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
class GetBookingsForUserUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private GetBookingsForUserUseCaseImpl getBookingsForUserUseCase;

    @Test
    void getBookingsForUser() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setUserId(user);
        booking.setPropertyId(new PropertyEntity());

        BookingEntity booking2 = new BookingEntity();
        booking2.setBookingId(2);
        booking2.setUserId(new UserEntity());
        booking2.setPropertyId(new PropertyEntity());

        List<BookingEntity> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);

        when(accessToken.getUserId()).thenReturn(user.getUserId());
        when(bookingRepository.findAll()).thenReturn(bookings);

        GetAllBookingsResponse response = getBookingsForUserUseCase.getBookingsForUser(user.getUserId());

        assertEquals(1, response.getAllBookings().size());
        assertEquals(1, response.getAllBookings().stream().findFirst().get().getUserId());

        verify(bookingRepository).findAll();
        verify(accessToken).getUserId();
    }

    @Test
    void getBookingsForUserNotFound() {
        Long id = 1L;

        List<BookingEntity> bookings = new ArrayList<>();

        when(accessToken.getUserId()).thenReturn(id);
        when(bookingRepository.findAll()).thenReturn(bookings);

        assertThrows(NoBookingsFoundException.class, () -> getBookingsForUserUseCase.getBookingsForUser(id));

        verify(bookingRepository).findAll();
        verify(accessToken).getUserId();
    }

    @Test
    void getBookingsNotAuthorized() {
        Long id = 1L;
        Long wrongId = 2L;

        when(accessToken.getUserId()).thenReturn(wrongId);

        assertThrows(UnauthorizedDataException.class, () -> getBookingsForUserUseCase.getBookingsForUser(id));

        verify(accessToken).getUserId();
    }
}