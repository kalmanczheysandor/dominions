package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth.AuthenticationFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth.AuthenticationSuccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.authentication.exception.NoAuthenticatedUserException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomAuthenticationFilter_b extends AbstractAuthenticationProcessingFilter {


    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON konverter


    public CustomAuthenticationFilter_b(CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager) {
        super("/data/auth/login",authenticationManager);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("attemptAuthentication: " + username + ":" + password);

        if (username == null || password == null) {
            throw new ServletException("Authentication failed: username or password is missing.");
        }

        // Hitelesítés delegálása az AuthenticationService-be
        Authentication auth = this.login(username, password);

        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);


//        System.out.println("Authentication successful for: " + authResult.getName());  // Log a sikeres autentikációról
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        if (securityContext.getAuthentication() != null) {
//            System.out.println("SecurityContext contains: " + securityContext.getAuthentication().getName());
//        } else {
//            System.out.println("SecurityContext is empty");
//        }




        List permissions = new ArrayList();

        if (authResult == null || !authResult.isAuthenticated()) {
            System.out.println("hasPermission:No 1");
            throw new RuntimeException("UHHHH");
        }


        SecurityUserDetails userDetails = (SecurityUserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();


        for (GrantedAuthority authority : authResult.getAuthorities()) {
            permissions.add(authority.getAuthority());
            System.out.println("hasPermission>>>>" + authority.getAuthority());

        }
        permissions.add("Breed:VIEW");
        //permissions.add("Breed:ADD");
        permissions.add("Breed:EDIT");

        // A sikeres autentikáció után válasz küldése
        AuthenticationSuccessResponse successResponse = new AuthenticationSuccessResponse(username, permissions);

        // JSON válasz küldése
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));



    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        AuthenticationFailureResponse failureResponse = new AuthenticationFailureResponse("Invalid username or password");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(failureResponse));
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    private Authentication login(String username, String password) {
        if (!customUserDetailsService.isEnabled(username)) {
            throw new UsernameNotFoundException("User is not enabled.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = getAuthenticationManager().authenticate(authenticationToken);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        System.out.println("Principal-Username:" +userDetails.getUsername());

        return auth;
    }


    private boolean isAnyUserAuthenticated() {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is not null and is authenticated
        return authentication != null && authentication.isAuthenticated()
            && !(authentication.getPrincipal() instanceof String);  // Principal is not 'anonymousUser'
    }


    private Integer getCurrentAuthenticatedUserId() {

        if (!isAnyUserAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }

        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication is present and the principal is an instance of the custom UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            return userDetails.getId();  // Retrieve the user ID from CustomUserDetails
        }

        // Return null or throw an exception if no user is authenticated
        return null;
    }


    private SecurityUserDetails getCurrentAuthenticatedUserDetails() {

        if (!isAnyUserAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }

        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication is present and the principal is an instance of the custom UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            return userDetails;
        }

        // Return null or throw an exception if no user is authenticated
        throw new RuntimeException();
    }



}
