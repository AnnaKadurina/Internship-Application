package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BookedDatesException extends ResponseStatusException {
    public BookedDatesException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_DATES");
    }
}