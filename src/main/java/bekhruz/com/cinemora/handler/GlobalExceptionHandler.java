package bekhruz.com.cinemora.handler;


import bekhruz.com.cinemora.dto.ResponseDto;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.exception.ValidationException;
import bekhruz.com.cinemora.util.ErrorUtil;
import bekhruz.com.cinemora.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        String message = getLastCause(ex);
        log.error(message, ex);
        return new ResponseEntity<>(new ResponseDto<>(ResponseDto.States.ERROR, 403, message),
                HttpStatus.FORBIDDEN);
    }

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(final BadCredentialsException ex) {
        String message = getLastCause(ex);
        log.error(message, ex);
        return new ResponseEntity<>(new ResponseDto<>(ResponseDto.States.ERROR, 401, message),
                HttpStatus.UNAUTHORIZED);
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

    @ExceptionHandler(RecordBadRequestException.class)
    public ResponseEntity<?> handleRecordBadRequestException(final RecordBadRequestException e) {
        log.error("RecordBadRequestException on: {}", ErrorUtil.getStacktrace(e));
        return new ResponseEntity<>(Map.of("message", List.of(translator.toLocale(e.getMessage()))),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExistsException(final AlreadyExistsException e) {
        log.error("AlreadyExistsException on: {}", ErrorUtil.getStacktrace(e));
        String message = translator.toLocale(e.getMessage());
        if (StringUtils.hasText(e.getField())) {
            message = message + " with this: " + e.getField();
        }
        return new ResponseEntity<>(Map.of("message", List.of(message)), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(GenericRuntimeException.class)
    public final ResponseEntity<?> handleGenericRuntimeException(final GenericRuntimeException e) {
        log.error("GenericRuntimeException on: {}", ErrorUtil.getStacktrace(e));
        return new ResponseEntity<>(Map.of("message", List.of(translator.toLocale(e.getMessage()))),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        String message = getLastCause(ex);
        log.error(message, ex);
        return new ResponseEntity<>(new ResponseDto<>(ResponseDto.States.ERROR, 401, message),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public final ResponseEntity<Object> handleJWTVerificationException(JWTVerificationException ex) {
        log.error("JWTVerificationException on: {}", ErrorUtil.getStacktrace(ex));
        return new ResponseEntity<>(Map.of("message", List.of(translator.toLocale(ex.getMessage()))),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JWTTokenExpiredException.class)
    public final ResponseEntity<Object> handleJWTTokenExpiredException(JWTTokenExpiredException ex) {
        log.error("JWTTokenExpiredException on: {}", ErrorUtil.getStacktrace(ex));
        return new ResponseEntity<>(Map.of("message", List.of(translator.toLocale(ex.getMessage()))),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }
}
