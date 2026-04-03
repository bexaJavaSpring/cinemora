package bekhruz.com.cinemora.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseDto<T> {
    public enum States {SUCCESS, ERROR, INFO, WARNING, EXPIRED_TEST_DATE, CONFLICT}

    private States state;

    private Integer statusCode;

    private String message;

    public ResponseDto(States state,  Integer statusCode, String message) {
        if (state != null) {
            setState(state);
        }

        if (statusCode != null) {
            setStatusCode(statusCode);
        }
        if (message != null) {
            setMessage(message);
        }
    }
}
