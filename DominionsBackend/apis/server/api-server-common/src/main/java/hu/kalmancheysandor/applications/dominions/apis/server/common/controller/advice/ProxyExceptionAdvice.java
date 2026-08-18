package hu.kalmancheysandor.applications.dominions.apis.server.common.controller.advice;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error.GeneralErrorResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(2)
@Slf4j
@RestControllerAdvice
public class ProxyExceptionAdvice {

    @ExceptionHandler(GeneralFailureResponseProxyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(GeneralFailureResponseProxyException exception) {
        // Logging
        log.warn("Proxy failure response redirected: {}", exception.getResponse());

        System.out.println("Advicer:GeneralFailureResponseProxyException");

        return  exception.getResponse();
    }

    @ExceptionHandler(GeneralErrorResponseProxyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse handle(GeneralErrorResponseProxyException exception) {
        // Logging
        log.warn("Proxy error response redirected: {}", exception.getResponse());

        return  exception.getResponse();
    }


    @ExceptionHandler(NotParseableResponseProxyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse handle(NotParseableResponseProxyException exception) {
        // Logging
        log.warn("Proxy response not parseable: {}", exception.getContent());

        // Generate response
        GeneralErrorResponse response = new GeneralErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }

    @ExceptionHandler(HttpStatusResponseProxyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse handle(HttpStatusResponseProxyException exception) {
        // Logging
        log.warn("Proxy response with HTTP status {}: {}", exception.getStatusCode(), exception.getMessage());

        // Generate response
        GeneralErrorResponse response = new GeneralErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("statusCode", String.valueOf(exception.getStatusCode()));
        return response;
    }




    @ExceptionHandler(BrokenResponseProxyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse handle(BrokenResponseProxyException exception) {
        log.warn("Proxy response is broken: {}", exception.getMessage());

        // Generate response
        GeneralErrorResponse response = new GeneralErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.DESTINATION_LOCKED)
    public GeneralErrorResponse handle(Exception exception) throws Exception {
        // Logging
        log.warn("EEEXCEPTION:"+exception.getClass().getSimpleName());
        exception.printStackTrace();
        throw exception;
//        System.err.println(exception);
//        // Generate response
//        GeneralErrorResponse response = new GeneralErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
//        return response;
    }
}
