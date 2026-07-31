package hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.SecurityFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.exception.SessionExpiredException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class TAccessDenyHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //
        SecurityFailureResponse failure = new SecurityFailureResponse(AccessDeniedException.class.getSimpleName());
        failure.addParameter("method",request.getMethod());
        failure.addParameter("uri",request.getRequestURI());
        failure.addParameter("url",request.getRequestURL());
        failure.addParameter("query",request.getQueryString());

        //
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(failure));
    }
}
