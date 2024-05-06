package hu.kalmanczheysandor.applications.dominion.exceptionhandling;

import hu.kalmanczheysandor.applications.dominion.exception.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors()
                                                .stream()
                                                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                                                .collect(Collectors.toList());

        // Send all error to logger
        for (FieldError fieldError : fieldErrors) {
            logAFieldError(fieldError);
        }
        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<List<FieldError>> handleException(UserNotFoundException exception) {
        FieldError fieldError = new FieldError("UserId", "User at id " + exception.getUserId() + " is not found!");
        logAFieldError(fieldError);
        return new ResponseEntity<>(List.of(fieldError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<String>> handleException(Exception exception) {
        log.error("An error was thrown. Details: field:"+exception.getMessage() + "'");
        return new ResponseEntity<>(List.of(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private void logAFieldError(FieldError fieldError) {
        log.error("An error was thrown. Details: field:" + fieldError.getField() + "; message:'" + fieldError.getErrorMessage() + "'");
    }
}
