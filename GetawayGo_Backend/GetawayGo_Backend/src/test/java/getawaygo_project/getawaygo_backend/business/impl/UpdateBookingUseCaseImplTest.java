package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookedDatesForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookedDatesException;
import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
import getawaygo_project.getawaygo_backend.domain.UpdateBookingRequest;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBookingUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;
    @InjectMocks
    private UpdateBookingUseCaseImpl updateBookingUseCase;

    @Test
    void updateBooking() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");
        Instant startDate2 = Instant.parse("2023-10-10T00:00:00Z");
        Instant endDate2 = Instant.parse("2023-10-20T00:00:00Z");

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setPropertyId(property);
        booking.setUserId(new UserEntity());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setPrice(100);

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate2)
                .endDate(endDate2)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(getBookedDatesForPropertyUseCase.getBookedDates(property.getPropertyId())).thenReturn(Collections.emptyList());

        updateBookingUseCase.updateBooking(request);

        assertEquals(startDate2, booking.getStartDate());
        assertEquals(endDate2, booking.getEndDate());

        verify(bookingRepository).findById(1L);
        verify(getBookedDatesForPropertyUseCase).getBookedDates(property.getPropertyId());
    }
    @Test
    void updateBookingStartDateIsAfterEndDate() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");
        Instant startDate2 = Instant.parse("2023-10-20T00:00:00Z");
        Instant endDate2 = Instant.parse("2023-10-10T00:00:00Z");

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setPropertyId(property);
        booking.setUserId(new UserEntity());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setPrice(100);

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate2)
                .endDate(endDate2)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(BookedDatesException.class, () -> updateBookingUseCase.updateBooking(request));

        verify(bookingRepository).findById(1L);
    }

    @Test
    void updateBookingDatesAreBooked() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        Instant oldStartDate = Instant.parse("2022-10-09T00:00:00Z");
        Instant oldEndDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant testDate = Instant.parse("2022-10-11T00:00:00Z");
        Instant testDate2 = Instant.parse("2022-10-12T00:00:00Z");
        Instant newStartDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant newEndDate = Instant.parse("2022-10-13T00:00:00Z");

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setPropertyId(property);
        booking.setUserId(new UserEntity());
        booking.setStartDate(oldStartDate);
        booking.setEndDate(oldEndDate);
        booking.setPrice(100);

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(getBookedDatesForPropertyUseCase.getBookedDates(property.getPropertyId())).thenReturn(new ArrayList<>(Arrays.asList(oldStartDate, oldEndDate, testDate2, testDate)));

        assertThrows(BookedDatesException.class, () -> updateBookingUseCase.updateBooking(request));

        verify(bookingRepository).findById(1L);
        verify(getBookedDatesForPropertyUseCase).getBookedDates(property.getPropertyId());
    }

    @Test
    void updateBookingNotFound() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> updateBookingUseCase.updateBooking(request));

        verify(bookingRepository).findById(1L);
    }
}