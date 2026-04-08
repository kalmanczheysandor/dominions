package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.error.GeneralErrorResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure.GeneralFailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@Order(99)
@RestControllerAdvice
public class GeneralGlobalExceptionHandler {


    // TODO: erre meg fel kell sziteni a frontendet

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FormErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException: ", exception);

        FormErrorResponse response = new FormErrorResponse();

        System.out.println("ExceptionHandler: A1");
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {

            ErrorItem errorItem = new ErrorItem(exception.getClass().getSimpleName(), exception.getMessage());
            errorItem.addParameter("FieldName", fieldError.getField());
            errorItem.addParameter("FieldMessage", fieldError.getDefaultMessage());

            response.addErrorItem(errorItem);
            //ErrorItem errorItem = new ErrorItem(fieldError.getField(), fieldError.getDefaultMessage());
            //System.out.println("Mezo["+fieldError.getField()+"]>>"+fieldError.getDefaultMessage());
            //errorItem.addParameter(fieldError.getField(),fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE )
//    public GeneralFailureResponse handleGeneralException(MethodArgumentNotValidException exception) {
//        log.error("Max upload size exceeded: ", exception);
//
//        // Generate response
//        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
//        response.addParameter("maxUploadSize", exception.getMaxUploadSize());
//
//        return response;
//    }
//





    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE )
    public GeneralFailureResponse handleGeneralException(MaxUploadSizeExceededException exception) {
        log.error("Max upload size exceeded: ", exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("maxUploadSize", exception.getMaxUploadSize());

        return response;
    }



    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralErrorResponse handleGeneralException(Exception exception) {
        log.error("Unhandled exception occurred: ", exception);
        return new GeneralErrorResponse("InternalServerError", "An unexpected error occurred.");
    }
}
