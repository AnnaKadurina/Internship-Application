package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleNotFoundOrValidException extends ResponseStatusException {
    public RoleNotFoundOrValidException(){
        super(HttpStatus.BAD_REQUEST, "ROLE_NOT_CORRECT");
    }
}
