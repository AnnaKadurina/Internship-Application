package getawaygo_project.getawaygo_backend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileException extends ResponseStatusException {
    public FileException(){
        super(HttpStatus.BAD_REQUEST, "FILE_ERROR");
    }
}