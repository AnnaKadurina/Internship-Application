package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoUsersFoundException extends ResponseStatusException {
    public NoUsersFoundException(){
        super(HttpStatus.NOT_FOUND, "USERS_NOT_FOUND");
    }
}
