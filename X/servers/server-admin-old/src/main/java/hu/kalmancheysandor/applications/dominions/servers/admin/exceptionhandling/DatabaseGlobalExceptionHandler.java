package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(1)
@RestControllerAdvice
public class DatabaseGlobalExceptionHandler {
//
//    @ExceptionHandler(DataAccessException.class)
//    public ResponseEntity<FormErrorResponse> handle(DataAccessException exception) {
//
//        ErrorItem errorItem = new ErrorItem(exception.getClass().getSimpleName(), exception.getMessage());
//
//        FormErrorResponse response = new FormErrorResponse();
//        response.addErrorItem(errorItem);
//
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    @ExceptionHandler(EntityExistsException.class)
//    public ResponseEntity<FormErrorResponse> handle(EntityExistsException exception) {
//        System.out.println("ExceptionHandler: D2");
//
//        ErrorItem errorItem = new ErrorItem(exception.getClass().getSimpleName(), exception.getMessage());
//
//        FormErrorResponse response = new FormErrorResponse();
//        response.addErrorItem(errorItem);
//
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<FormErrorResponse> handle(EntityNotFoundException exception) {
//        System.out.println("ExceptionHandler: D3");
//        ErrorItem errorItem = new ErrorItem(exception.getClass().getSimpleName(), exception.getMessage());
//
//        FormErrorResponse response = new FormErrorResponse();
//        response.addErrorItem(errorItem);
//
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


}
