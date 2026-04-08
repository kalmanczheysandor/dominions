package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class CustomAuthenticationFilter_old extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationFilter_old(String defaultFilterProcessesUrl, AuthenticationManager authManager) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authRequest);
    }

//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        System.out.println("method: successfulAuthentication");
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write("Login successful");
//        System.out.println("Authorities: ");
//
//        authResult.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority()));
//    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("Authentication successful: " + authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        System.out.println("Authentication set in context: " + SecurityContextHolder.getContext().getAuthentication());

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Login successful");


        HttpSession session = request.getSession();
        System.out.println("Session ID: " + session.getId());
    }



    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("method: Unsuccessful authentication");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Login failed: " + failed.getMessage());
    }
}