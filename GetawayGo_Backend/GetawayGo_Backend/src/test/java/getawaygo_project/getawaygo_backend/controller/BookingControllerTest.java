package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.domain.*;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private GetBookingsForPropertyUseCase getBookingsForPropertyUseCase;
    @Mock
    private GetBookingsForUserUseCase getBookingsForUserUseCase;
    @Mock
    private DeleteBookingUseCase deleteBookingUseCase;
    @Mock
    private UpdateBookingUseCase updateBookingUseCase;
    @Mock
    private CreateBookingUseCase createBookingUseCase;
    @Mock
    private GetBookingUseCase getBookingUseCase;
    @Mock
    private GenerateBookingFileUseCase generateBookingFileUseCase;
    @Mock
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;
    @InjectMocks
    private BookingController bookingController;

    @Test
    void createBooking() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .build();
        Booking booking = Booking.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .price(100)
                .build();
        CreateBookingResponse response = CreateBookingResponse.builder()
                .bookingResponse(booking)
                .build();

        when(createBookingUseCase.createBooking(request)).thenReturn(response);

        ResponseEntity<CreateBookingResponse> createResponse = bookingController.createBooking(request);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals(request.getStartDate(), createResponse.getBody().getBookingResponse().getStartDate());

        verify(createBookingUseCase).createBooking(request);

    }

    @Test
    void getBooking() {
        Booking booking = new Booking();
        booking.setBookingId(1);

        when(getBookingUseCase.getBooking(booking.getBookingId())).thenReturn(Optional.of(booking));

        ResponseEntity<Booking> getBooking = bookingController.getBooking(booking.getBookingId());

        assertEquals(getBooking.getBody(), booking);
        assertEquals(getBooking.getBody().getBookingId(), booking.getBookingId());
        assertEquals(HttpStatus.OK, getBooking.getStatusCode());

        verify(getBookingUseCase).getBooking(booking.getBookingId());

    }

    @Test
    void getBookedDatesForProperty() {
        Instant bookedDate1 = Instant.parse("2022-10-10T00:00:00Z");
        Instant bookedDate2 = Instant.parse("2022-10-11T00:00:00Z");

        List<Instant> bookedDates = List.of(bookedDate1, bookedDate2);


        when(getBookedDatesForPropertyUseCase.getBookedDates(1)).thenReturn(bookedDates);

        ResponseEntity<List<Instant>> bookingDatesResponse = bookingController.getBookedDatesForProperty(1);

        assertEquals(bookedDates, bookingDatesResponse.getBody());
        assertEquals(HttpStatus.OK, bookingDatesResponse.getStatusCode());

        verify(getBookedDatesForPropertyUseCase).getBookedDates(1);
    }

    @Test
    void getBookingsForUser() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        bookings.add(new Booking());

        GetAllBookingsResponse response = new GetAllBookingsResponse(bookings);

        when(getBookingsForUserUseCase.getBookingsForUser(user.getUserId())).thenReturn(response);

        ResponseEntity<GetAllBookingsResponse> bookingsResponse = bookingController.getBookingsForUser(user.getUserId());

        assertEquals(response, bookingsResponse.getBody());
        assertEquals(HttpStatus.OK, bookingsResponse.getStatusCode());

        verify(getBookingsForUserUseCase).getBookingsForUser(user.getUserId());
    }

    @Test
    void getBookingsForProperty() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        bookings.add(new Booking());

        GetAllBookingsResponse response = new GetAllBookingsResponse(bookings);

        when(getBookingsForPropertyUseCase.getBookingsForProperty(property.getPropertyId())).thenReturn(response);

        ResponseEntity<GetAllBookingsResponse> bookingsResponse = bookingController.getBookingsForProperty(property.getPropertyId());

        assertEquals(response, bookingsResponse.getBody());
        assertEquals(HttpStatus.OK, bookingsResponse.getStatusCode());

        verify(getBookingsForPropertyUseCase).getBookingsForProperty(property.getPropertyId());
    }

    @Test
    void updateBooking() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        doNothing().when(updateBookingUseCase).updateBooking(request);
        ResponseEntity<Void> update = bookingController.updateBooking(request);

        assertEquals(HttpStatus.NO_CONTENT, update.getStatusCode());
        verify(updateBookingUseCase).updateBooking(request);
    }

    @Test
    void deleteBooking() {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setPropertyId(1);
        booking.setUserId(1);

        doNothing().when(deleteBookingUseCase).deleteBooking(booking.getBookingId());

        ResponseEntity<Void> deleteResult = bookingController.deleteBooking(booking.getBookingId());

        assertEquals(HttpStatus.NO_CONTENT, deleteResult.getStatusCode());

        verify(deleteBookingUseCase).deleteBooking(booking.getBookingId());

    }

    @Test
    void generateFile() {
        Long id = 1L;
        String url = "test";

        when(generateBookingFileUseCase.generateFile(id)).thenReturn(url);

        ResponseEntity<String> generatedUrl = bookingController.generateBookingFile(id);

        assertEquals(HttpStatus.OK, generatedUrl.getStatusCode());
        verify(generateBookingFileUseCase).generateFile(id);
    }
}