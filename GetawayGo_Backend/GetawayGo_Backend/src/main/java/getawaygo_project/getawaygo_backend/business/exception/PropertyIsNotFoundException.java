package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PropertyIsNotFoundException extends ResponseStatusException {
    public PropertyIsNotFoundException(){
        super(HttpStatus.NOT_FOUND, "PROPERTY_NOT_FOUND");
    }
}
