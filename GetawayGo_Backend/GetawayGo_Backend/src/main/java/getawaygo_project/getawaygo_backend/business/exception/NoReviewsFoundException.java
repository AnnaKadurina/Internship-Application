package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoReviewsFoundException extends ResponseStatusException {
    public NoReviewsFoundException(){
        super(HttpStatus.NOT_FOUND, "REVIEWS_NOT_FOUND");
    }
}
