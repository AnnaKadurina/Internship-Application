package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BookingNotFoundException extends ResponseStatusException {
    public BookingNotFoundException(){
        super(HttpStatus.NOT_FOUND, "BOOKING_NOT_FOUND");
    }
}
