package hu.kalmancheysandor.applications.dominions.servers.game.exceptionhandling;

import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception.PlayerEndpointKeyAlreadyRegisteredSessionException;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception.UserUuidAlreadyRegisteredSessionException;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionNotRecruitingException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@RestControllerAdvice
public class GameSessionExceptionHandler {

    @ExceptionHandler(GameSessionNotRecruitingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(GameSessionNotRecruitingException exception) {
        // Logging
        log.warn("Game session is not recruiting: {}", exception.getUuid());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("gameSessionUuid", exception.getUuid());

        return response;
    }





    @ExceptionHandler(PlayerEndpointKeyAlreadyRegisteredSessionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(PlayerEndpointKeyAlreadyRegisteredSessionException exception) {
        // Logging
        log.warn("Game session {} is already containing the secret key: {}", exception.getSessionKey(),exception.getEndpointKey());

        System.out.println("PlayerEndpointKeyAlreadyRegisteredSessionException");


        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("gameSessionUuid", exception.getSessionKey());
        response.addParameter("endpointKey", exception.getEndpointKey());

        return response;
    }


    @ExceptionHandler(UserUuidAlreadyRegisteredSessionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(UserUuidAlreadyRegisteredSessionException exception) {
        // Logging
        log.warn("Game session {} is already containing the user uuid: {}", exception.getSessionKey(), exception.getUserUuid());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("gameSessionUuid", exception.getSessionKey());
        response.addParameter("userUuid", exception.getUserUuid());

        return response;
    }
}
