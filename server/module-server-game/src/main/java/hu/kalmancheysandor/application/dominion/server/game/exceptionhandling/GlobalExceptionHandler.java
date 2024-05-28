package hu.kalmancheysandor.application.dominion.server.game.exceptionhandling;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action.AttackingOwnCellPlayerActionException;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action.OutOfAttackRangePlayerActionException;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action.SelfAttackPlayerActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionBox> handle(Exception exception) {
//        System.out.println("ADVICER: " + exception.getClass().getSimpleName());
//        ExceptionBox exceptionBox = new ExceptionBox(exception.getClass().getCanonicalName().toString(),exception);
//        return new ResponseEntity<>(exceptionBox, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(OutOfAttackRangePlayerActionException.class)
    public ResponseEntity<?> handle(OutOfAttackRangePlayerActionException exception) {
        System.out.println("ADVICER: " + exception.getClass().getSimpleName());
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());
        errorDetails.getParameters().put("cellKey", exception.getTargetedCellKey());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SelfAttackPlayerActionException.class)
    public ResponseEntity<SelfAttackPlayerActionException> handle(SelfAttackPlayerActionException exception) {
        System.out.println("ADVICER: " + exception.getClass().getSimpleName());
        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//
//    // Handle global exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
//        ErrorDetails errorDetails = new ErrorDetails(ex.get, request.getDescription(false));
//
//
//
//
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }
//


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<List<FieldError>> handleValidationException(MethodArgumentNotValidException exception) {
//        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors()
//                                                .stream()
//                                                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
//                                                .collect(Collectors.toList());
//
//        // Send all error to logger
//        for (FieldError fieldError : fieldErrors) {
//            logAFieldError(fieldError);
//        }
//        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
//    }
//
//
//
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<List<FieldError>> handleException(UserNotFoundException exception) {
//        FieldError fieldError = new FieldError("UserId", "User at id " + exception.getUserId() + " is not found!");
//        logAFieldError(fieldError);
//        return new ResponseEntity<>(List.of(fieldError), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<List<String>> handleException(Exception exception) {
//        log.error("An error was thrown. Details: field:"+exception.getMessage() + "'");
//        return new ResponseEntity<>(List.of(exception.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    private void logAFieldError(FieldError fieldError) {
//        log.error("An error was thrown. Details: field:" + fieldError.getField() + "; message:'" + fieldError.getErrorMessage() + "'");
//    }
}
