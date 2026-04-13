package bekhruz.com.cinemora.handler;


import bekhruz.com.cinemora.dto.response.ResponseDto;
import bekhruz.com.cinemora.exception.CustomAccessDeniedException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.exception.ValidationException;
import bekhruz.com.cinemora.util.ErrorUtil;
import bekhruz.com.cinemora.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Translator translator;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleError(final MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException on: {}", ErrorUtil.getStacktrace(ex));
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                {
                    if (!translator.toLocale(fieldError.getDefaultMessage()).equals(fieldError.getDefaultMessage())) {
                        return Objects.requireNonNull(translator.toLocale(fieldError.getDefaultMessage()));
                    } else {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }

                }).toList();
        return new ResponseEntity<>(Map.of("message", errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(GenericNotFoundException.class)
    public ResponseEntity<?> handleGenericNotFoundException(final GenericNotFoundException e) {
        log.error("CustomNotFoundException on: {}", ErrorUtil.getStacktrace(e));
        String message = translator.toLocale(e.getMessage());
        if (StringUtils.hasText(e.getField())) {
            message = message + " with this: " + e.getField();
        }
        return new ResponseEntity<>(Map.of("message", List.of(message)), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidatorException(final ValidationException e) {
        log.error("ValidatorException on: {}", ErrorUtil.getStacktrace(e));
        return new ResponseEntity<>(Map.of("message", List.of(translator.toLocale(e.getMessage()))),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<?> handleCustomAccessDeniedException(final CustomAccessDeniedException e) {
        log.error("CustomAccessDeniedException on: {}", ErrorUtil.getStacktrace(e));
        return new ResponseEntity<>(Map.of("message", List.of(translator.toLocale(e.getMessage()))),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class, Throwable.class})
    public final ResponseEntity<Object> handleException(Exception ex) {
        String message = getLastCause(ex);
        String value = translator.toLocale(message);
        log.error(message, ex);
        if (ex.getMessage().equals("Full authentication is required to access this resource")) {
            return new ResponseEntity<>(new ResponseDto<>(ResponseDto.States.ERROR, 401,
                    value), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new ResponseDto<>(ResponseDto.States.ERROR, 400,
                value), HttpStatus.BAD_REQUEST);
    }

    private String getLastCause(Throwable throwable) {
        return throwable.getCause() == null ? (throwable.getLocalizedMessage() == null ? throwable.getMessage()
                : throwable.getLocalizedMessage()) : getLastCause(throwable.getCause());
    }
}
