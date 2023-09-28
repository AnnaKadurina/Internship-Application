package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoPropertiesFoundException extends ResponseStatusException {
    public NoPropertiesFoundException(){
        super(HttpStatus.NOT_FOUND, "PROPERTIES_NOT_FOUND");
    }
}