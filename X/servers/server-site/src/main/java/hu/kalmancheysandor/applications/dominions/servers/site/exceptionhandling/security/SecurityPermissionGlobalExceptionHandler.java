package hu.kalmancheysandor.applications.dominions.servers.site.exceptionhandling.security;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.SecurityFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@RestControllerAdvice

public class SecurityPermissionGlobalExceptionHandler {

    @ExceptionHandler(ActionNotGrantedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SecurityFailureResponse handle(ActionNotGrantedException exception) {
        
        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("resource",exception.getResource());
        response.addParameter("action",exception.getAction());
        return response;
    }

    @ExceptionHandler(AccessActionNotGrantedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SecurityFailureResponse handle(AccessActionNotGrantedException exception) {

        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("resource",exception.getResource());
        response.addParameter("action",exception.getAction());
        return response;
    }
    
    @ExceptionHandler(AddActionNotGrantedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SecurityFailureResponse handle(AddActionNotGrantedException exception) {

        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("resource",exception.getResource());
        response.addParameter("action",exception.getAction());
        return response;
    }


    @ExceptionHandler(EditActionNotGrantedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SecurityFailureResponse handle(EditActionNotGrantedException exception) {

        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("resource",exception.getResource());
        response.addParameter("action",exception.getAction());
        return response;
    }

    @ExceptionHandler(DeleteActionNotGrantedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SecurityFailureResponse handle(DeleteActionNotGrantedException exception) {

        SecurityFailureResponse response = new SecurityFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("resource",exception.getResource());
        response.addParameter("action",exception.getAction());
        return response;
    }

}
