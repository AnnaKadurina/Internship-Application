package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoBookingsFoundException extends ResponseStatusException {
    public NoBookingsFoundException(){
        super(HttpStatus.NOT_FOUND, "BOOKINGS_NOT_FOUND");
    }
}
