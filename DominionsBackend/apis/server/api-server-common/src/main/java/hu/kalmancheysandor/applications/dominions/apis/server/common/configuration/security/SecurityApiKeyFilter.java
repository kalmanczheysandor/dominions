package hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class SecurityApiKeyFilter extends OncePerRequestFilter {

    private final String secretApiKey;

    public SecurityApiKeyFilter(String secretApiKey) {
        this.secretApiKey = secretApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String receivedKey = request.getHeader("X-Service-Key");
        String serviceName = request.getHeader("X-Service-Name");

        System.out.println("+++ SecurityApiKeyFilter +++");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("KEY: " + receivedKey);
        System.out.println("SERVICE: " + serviceName);


        // Error checking: Test whether service-key exists and is not blank
        if (receivedKey == null || receivedKey.isBlank()) {
            System.out.println("[ApyKeyBasedAuthentication]: Error: #SERVICE KEY MISSING ");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Service key is missing");
            return;
        }
        // Error checking: Test whether service-name exists and is not blank
        if (serviceName == null || serviceName.isBlank()) {
            System.out.println("[ApyKeyBasedAuthentication]: Error: #SERVICE NAME MISSING ");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Service name is missing");
            return;
        }
        // Error checking: Test whether service-key is valid
        if (!secretApiKey.equals(receivedKey)) {
            System.out.println("[ApyKeyBasedAuthentication]: Error: #API KEY INVALID ");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid service key");
            return;
        }

        System.out.println("ApiKey and ServiceName is accepted");

        // Retrieve the existing authentication, if any.
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        // Error checking: Test whether the authenticated user is only an Anonymous user
        if (currentAuthentication != null && currentAuthentication.isAuthenticated() && (currentAuthentication instanceof AnonymousAuthenticationToken)) {
            System.out.println("[SessionBasedAuthentication]: Error: Anonymous user ");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Anonymous session authentication is not allowed");
        }

        // Use session authentication
        if (currentAuthentication != null && currentAuthentication.isAuthenticated() && !(currentAuthentication instanceof AnonymousAuthenticationToken)) {
            System.out.println("[SessionBasedAuthentication]: Success, the user is:" + currentAuthentication.getName());
            filterChain.doFilter(request, response);
            return;
        }


        // Use service-to-service authentication
        // due to no session authentication is available, but API key is valid
        {
            System.out.println("[ApyKeyBasedAuthentication]: Authentication creation ");

            // Initialise new service-to-service authentication object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    serviceName,
                    null,
                    List.of()
            );

            // Add authentication to context holder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }

    }
}
