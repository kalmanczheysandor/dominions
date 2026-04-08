package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.security;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.SecurityFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.NoAuthenticationException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.SessionExpiredException;
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
public class AuthenticationGlobalExceptionHandler {


}
