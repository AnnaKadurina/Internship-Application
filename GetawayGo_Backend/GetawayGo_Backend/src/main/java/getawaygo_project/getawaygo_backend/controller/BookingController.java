package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.configuration.isauthenticated.IsAuthenticated;
import getawaygo_project.getawaygo_backend.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class BookingController {
    private CreateBookingUseCase createBookingUseCase;
    private GetBookingsForUserUseCase getBookingsForUserUseCase;
    private GetBookingsForPropertyUseCase getBookingsForPropertyUseCase;
    private UpdateBookingUseCase updateBookingUseCase;
    private DeleteBookingUseCase deleteBookingUseCase;
    private GetBookingUseCase getBookingUseCase;
    private GenerateBookingFileUseCase generateBookingFileUseCase;
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;

    @IsAuthenticated
    @RolesAllowed("ROLE_GUEST")
    @PostMapping()
    public ResponseEntity<CreateBookingResponse> createBooking(@RequestBody @Valid CreateBookingRequest request) {
        CreateBookingResponse response = createBookingUseCase.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_HOST", "ROLE_GUEST"})
    @GetMapping("/booked/{propertyId}")
    public ResponseEntity<List<Instant>> getBookedDatesForProperty(@PathVariable("propertyId") long propertyId) {
        List<Instant> response = getBookedDatesForPropertyUseCase.getBookedDates(propertyId);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_GUEST")
    @GetMapping("/user/{userId}")
    public ResponseEntity<GetAllBookingsResponse> getBookingsForUser(@PathVariable("userId") long userId) {
        GetAllBookingsResponse response = getBookingsForUserUseCase.getBookingsForUser(userId);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_HOST")
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<GetAllBookingsResponse> getBookingsForProperty(@PathVariable("propertyId") long propertyId) {
        GetAllBookingsResponse response = getBookingsForPropertyUseCase.getBookingsForProperty(propertyId);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_HOST", "ROLE_GUEST"})
    @GetMapping("{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable(value = "id") final long id) {
        final Optional<Booking> booking = getBookingUseCase.getBooking(id);
        return ResponseEntity.ok().body(booking.get());
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_GUEST")
    @PutMapping()
    public ResponseEntity<Void> updateBooking(@RequestBody @Valid UpdateBookingRequest request) {
        updateBookingUseCase.updateBooking(request);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_HOST", "ROLE_GUEST"})
    @DeleteMapping("{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long bookingId) {
        deleteBookingUseCase.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirmation/{id}")
    public ResponseEntity<String> generateBookingFile(@PathVariable long id) {
        String url = generateBookingFileUseCase.generateFile(id);
        return ResponseEntity.ok().body(url);
    }
}


