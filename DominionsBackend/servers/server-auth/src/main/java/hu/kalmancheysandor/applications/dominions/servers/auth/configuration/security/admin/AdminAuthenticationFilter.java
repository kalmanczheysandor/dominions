package hu.kalmancheysandor.applications.dominions.servers.auth.configuration.security.admin;


import com.fasterxml.jackson.databind.ObjectMapper;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.AuthenticationFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.security.AdminUserSecurityDetails;
import hu.kalmancheysandor.applications.dominions.servers.auth.dto.admin.AdminAuthenticationFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.auth.dto.admin.AdminAuthenticationSuccessResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.AdminUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdminAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AdminUserDetailsService adminUserDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String endpoint;

    public AdminAuthenticationFilter(String endpoint,AuthenticationManager adminAuthenticationManager, AdminUserDetailsService adminUserDetailsService) {
        super(endpoint);
        setAuthenticationManager(adminAuthenticationManager);
        this.adminUserDetailsService = adminUserDetailsService;
        this.endpoint = endpoint;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(identifier, password);

        System.out.println("------------Attempt to authenticate [AdminAuthenticationFilter]-------------");
        System.out.println("Identifier:" + identifier);
        System.out.println("Password:" + password);
        System.out.println("Cookies:" + request.getHeader("Cookie"));


        return getAuthenticationManager().authenticate(authRequest);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        SecurityContextHolder.getContext().setAuthentication(authResult);

        // TODO: It is a temporary solution!
        // Source: https://github.com/spring-projects/spring-security/issues/9173
        // Get the right session
        request.getSession()
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext()
                );

        // Determine session-id
        HttpSession session = request.getSession(true);
        session.setAttribute("SESSION_TYPE", "ADMIN");
        String sessionId = session.getId();

        // Safety checking: is authentication done successfully.
        if (authResult == null || !authResult.isAuthenticated()) {
            throw new RuntimeException("Method should not be called before successful authentication!");
        }

        // Safety checking: whether the user data is accessible
        AdminUserSecurityDetails userDetails = (AdminUserSecurityDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();
        int userId = userDetails.getId();
        AdminUser adminUser = adminUserDetailsService.findUserById(userId);
        if (adminUser == null) {
            throw new RuntimeException("User not found!");
        }

        // Collect permissions inorder to inform the frontend-level authorisation framework
        List<String> permissions = new ArrayList();
        for (GrantedAuthority authority : authResult.getAuthorities()) {
            permissions.add(authority.getAuthority());
        }

        // Generate response object
        AdminAuthenticationSuccessResponse successResponse = AdminAuthenticationSuccessResponse.builder()
                .identifier(username)
                .permissions(permissions)
                .sessionId(sessionId)
                .userUuid(adminUser.getUuid())
                .endpoint(endpoint)
                .build();

        // Inject response object
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        AdminAuthenticationFailureResponse failureResponse;

        // Generate response object
        if (exception instanceof BadCredentialsException) {
            failureResponse = new AdminAuthenticationFailureResponse(exception.getClass().getSimpleName(),"Invalid username or password",endpoint);
        } else if (exception instanceof LockedException) {
            failureResponse = new AdminAuthenticationFailureResponse(exception.getClass().getSimpleName(),"Your account is locked",endpoint);
        } else if (exception instanceof DisabledException) {
            failureResponse = new AdminAuthenticationFailureResponse(exception.getClass().getSimpleName(),"Your account is disabled",endpoint);
        } else if (exception instanceof AccountExpiredException) {
            failureResponse = new AdminAuthenticationFailureResponse(exception.getClass().getSimpleName(),"Your account has expired",endpoint);
        } else if (exception instanceof CredentialsExpiredException) {
            failureResponse = new AdminAuthenticationFailureResponse(exception.getClass().getSimpleName(),"Your password has expired",endpoint);
        } else {
            failureResponse = new AdminAuthenticationFailureResponse(exception.getClass().getSimpleName(),"Authentication failed!",endpoint);
        }

        // Inject response object
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(failureResponse));
    }


}
