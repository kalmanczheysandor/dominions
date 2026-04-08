package hu.kalmancheysandor.applications.dominions.servers.site.exceptionhandling.security;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.SecurityFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.SessionExpiredException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.authentication.exception.UserIsNotAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Order(1)
@RestControllerAdvice
public class SecurityGlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SecurityFailureResponse handle(AccessDeniedException exception) {
        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }


    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SecurityFailureResponse handle(NoResourceFoundException exception) {
        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }


    @ExceptionHandler(SessionExpiredException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SecurityFailureResponse handle(SessionExpiredException exception) {
        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }


    @ExceptionHandler(UserIsNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SecurityFailureResponse handle(UserIsNotAuthenticatedException exception) {
        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }

}
