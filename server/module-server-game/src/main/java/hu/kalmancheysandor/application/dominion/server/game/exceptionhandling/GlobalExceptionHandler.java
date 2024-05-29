package hu.kalmancheysandor.application.dominion.server.game.exceptionhandling;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.*;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action.*;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// GAME - ACTION EXCEPTIONS ////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(OutOfAttackRangePlayerActionException.class)
    public ResponseEntity<?> handle(OutOfAttackRangePlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());
        errorDetails.getParameters().put("cellKey", exception.getTargetedCellKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SelfAttackPlayerActionException.class)
    public ResponseEntity<?> handle(SelfAttackPlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OutOfDoughnutAttackRangePlayerActionException.class)
    public ResponseEntity<?> handle(OutOfDoughnutAttackRangePlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoTroopsWereSentPlayerActionException.class)
    public ResponseEntity<?> handle(NoTroopsWereSentPlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TooMuchTroopsWereSentPlayerActionException.class)
    public ResponseEntity<?> handle(TooMuchTroopsWereSentPlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoTroopsPermittedToSendPlayerActionException.class)
    public ResponseEntity<?> handle(NoTroopsPermittedToSendPlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotEnoughSupplyPlayerActionException.class)
    public ResponseEntity<?> handle(NotEnoughSupplyPlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AttackingOwnCellPlayerActionException.class)
    public ResponseEntity<?> handle(AttackingOwnCellPlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlayerActionException.class)
    public ResponseEntity<?> handle(PlayerActionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// GAME EXCEPTIONS /////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ExceptionHandler(UnexpectedCaseFoundGameException.class)
    public ResponseEntity<?> handle(UnexpectedCaseFoundGameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnableToOpenMapFileGameException.class)
    public ResponseEntity<?> handle(UnableToOpenMapFileGameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("path", exception.getPath());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoWinnerDeterminedYetGameException.class)
    public ResponseEntity<?> handle(NoWinnerDeterminedYetGameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GameAlreadyEndedGameException.class)
    public ResponseEntity<?> handle(GameAlreadyEndedGameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlayerAlreadyDeadGameException.class)
    public ResponseEntity<?> handle(PlayerAlreadyDeadGameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("playerKey", exception.getPlayerKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GeneralGameException.class)
    public ResponseEntity<?> handle(GeneralGameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GameException.class)
    public ResponseEntity<?> handle(GameException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// SESSION EXCEPTIONS //////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(PlayerKeyNotNotMemberOfHumanPlayerSlotSessionException.class)
    public ResponseEntity<?> handle(PlayerKeyNotNotMemberOfHumanPlayerSlotSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());
        errorDetails.getParameters().put("playerId", exception.getPlayerId());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlayerKeyNotNotMemberOfAiPlayerSlotSessionException.class)
    public ResponseEntity<?> handle(PlayerKeyNotNotMemberOfAiPlayerSlotSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());
        errorDetails.getParameters().put("playerId", exception.getPlayerId());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlayerKeyAlreadyIssuedSessionException.class)
    public ResponseEntity<?> handle(PlayerKeyAlreadyIssuedSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());
        errorDetails.getParameters().put("playerId", exception.getPlayerId());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PendingTurnSessionException.class)
    public ResponseEntity<?> handle(PendingTurnSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotExistingPlayerSessionException.class)
    public ResponseEntity<?> handle(NotExistingPlayerSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotExistingInstanceSessionException.class)
    public ResponseEntity<?> handle(NotExistingInstanceSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoMoreFreePlayerSlotSessionException.class)
    public ResponseEntity<?> handle(NoMoreFreePlayerSlotSessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IntentionIsAlreadyGivenException.class)
    public ResponseEntity<?> handle(IntentionIsAlreadyGivenException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateGamePlaySessionException.class)
    public ResponseEntity<?> handle(DuplicateGamePlaySessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SessionException.class)
    public ResponseEntity<?> handle(SessionException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getClass().getSimpleName());
        errorDetails.getParameters().put("sessionKey", exception.getSessionKey());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
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
