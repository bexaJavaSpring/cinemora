package bekhruz.com.cinemora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericRuntimeException extends RuntimeException {
    public GenericRuntimeException(String message){
        super(message);
    }
}
