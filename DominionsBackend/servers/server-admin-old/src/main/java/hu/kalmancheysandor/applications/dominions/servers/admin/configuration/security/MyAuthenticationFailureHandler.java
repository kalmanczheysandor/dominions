package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.ErrorItem;
import hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.FormErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        ErrorItem errorItem = new ErrorItem(exception.getClass().getSimpleName(), exception.getMessage());
        FormErrorResponse errorResponse = new FormErrorResponse();
        errorResponse.addErrorItem(errorItem);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(errorResponse);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
