package hu.kalmancheysandor.applications.dominions.servers.site.configuration.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityContextDebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            System.out.println("❌ [SITE] No Authentication found in SecurityContext");
        } else {
            System.out.println("✅ [SITE] Authenticated user: "
                    + auth.getName()
                    + " | authorities=" + auth.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }
}

