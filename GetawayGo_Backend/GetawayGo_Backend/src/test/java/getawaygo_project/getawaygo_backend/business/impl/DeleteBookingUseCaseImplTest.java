package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.ReviewNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteBookingUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private DeleteBookingUseCaseImpl deleteBookingUseCase;

    @Test
    void deleteBooking() {
        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setUserId(new UserEntity());
        booking.setPropertyId(new PropertyEntity());

        when(bookingRepository.existsById(booking.getBookingId())).thenReturn(true);
        deleteBookingUseCase.deleteBooking(booking.getBookingId());

        assertEquals(Optional.empty(), bookingRepository.findById(booking.getBookingId()));

        verify(bookingRepository).existsById(booking.getBookingId());
        verify(bookingRepository).deleteById(booking.getBookingId());
    }
    @Test
    void deleteBookingNotFound() {
        Long id = 1L;

        when(bookingRepository.existsById(id)).thenReturn(false);

        assertThrows(BookingNotFoundException.class, () -> deleteBookingUseCase.deleteBooking(id));

        verify(bookingRepository).existsById(id);
    }
}