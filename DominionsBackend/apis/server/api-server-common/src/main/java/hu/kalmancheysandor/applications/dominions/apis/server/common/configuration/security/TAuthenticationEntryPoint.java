package hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.SecurityFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.exception.SessionExpiredException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

public class TAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        SecurityFailureResponse failureResponse = null;
        boolean hadSession = request.getRequestedSessionId() != null;
        boolean sessionInvalid = !request.isRequestedSessionIdValid();

        if (hadSession && sessionInvalid) {
            failureResponse = new SecurityFailureResponse(SessionExpiredException.class.getSimpleName(), "Session expired");
        } else {
            if (authException instanceof InsufficientAuthenticationException) {
                failureResponse = new SecurityFailureResponse(InsufficientAuthenticationException.class.getSimpleName(), "Insufficient authentication exception");
            }
            else if (authException instanceof AuthenticationCredentialsNotFoundException) {
                failureResponse = new SecurityFailureResponse(AuthenticationCredentialsNotFoundException.class.getSimpleName(), "Authentication credentials not found");
            }
            else {
                failureResponse = new SecurityFailureResponse(authException.getClass().getSimpleName(), "????????");
            }

        }

        if(failureResponse!=null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(failureResponse));
        }
    }
}
