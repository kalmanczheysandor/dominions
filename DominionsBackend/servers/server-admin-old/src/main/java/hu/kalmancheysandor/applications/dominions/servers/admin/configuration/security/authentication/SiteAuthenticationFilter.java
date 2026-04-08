package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.SecurityUserDetails;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth.AuthenticationFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth.AuthenticationSuccessResponse;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SiteAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    //private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON konverter

    public SiteAuthenticationFilter(AuthenticationManager authManager) {
        super("/data/auth/login2");
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("AUTH");
        System.out.println("SiteAuthenticationFilter.attemptAuthentication");
//        System.out.println("Identifier:"+identifier);
//        System.out.println("Password:"+password);
//        System.out.println("Encode:"+passwordEncoder.encode(password));

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(identifier, password);

        return getAuthenticationManager().authenticate(authRequest);
    }











    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////








    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);


        // TODO: Ez egy ideiglenes megoldas. Talalni kell egy normalisat. Forras:https://github.com/spring-projects/spring-security/issues/9173
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//

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




}
