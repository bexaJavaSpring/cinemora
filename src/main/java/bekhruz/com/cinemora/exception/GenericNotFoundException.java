package bekhruz.com.cinemora.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
public class GenericNotFoundException extends RuntimeException {
    private String field;

    public GenericNotFoundException(String message) {
        super(message);
    }

    public GenericNotFoundException(String field, String message) {
        super(message);
        this.field = field;
    }
}
