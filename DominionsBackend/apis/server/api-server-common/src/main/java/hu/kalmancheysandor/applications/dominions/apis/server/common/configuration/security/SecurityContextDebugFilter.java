package hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityContextDebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestedSessionId = request.getRequestedSessionId();
        HttpSession session = request.getSession(false);

        System.out.println("---- SESSION DEBUG ----");
        System.out.println("Requested Session ID: " + requestedSessionId);

        if (session != null) {
            System.out.println("Actual Session ID: " + session.getId());
        } else {
            System.out.println("No HttpSession exists");
        }


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            System.out.println("NO [GAME] No Authentication found in SecurityContext");
        } else {
            System.out.println("YES [GAME] Authenticated user: "
                    + auth.getName()
                    + " | authorities=" + auth.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }
}

