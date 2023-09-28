package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProfileDeactivatedException extends ResponseStatusException {
    public ProfileDeactivatedException(){
        super(HttpStatus.BAD_REQUEST, "PROFILE_INVALID");
    }
}
