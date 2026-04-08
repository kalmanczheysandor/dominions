package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.security.AdminUserSecurityDetails;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.AdminUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

//    @Autowired
    private AdminUserDetailsService adminUserDetailsService; // Ahol a session adatait lekérdezzük

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal - A1");
        HttpSession session = request.getSession(false); // Ellenőrizzük, hogy van-e session
        if (session != null) {
            System.out.println("doFilterInternal - A2");
            String sessionId = session.getId();  // Lekérjük a session ID-t
            String username = (String) session.getAttribute("username"); // A felhasználónév a session-ből

            if (username != null) {
                System.out.println("doFilterInternal - A3");
                // Ha létezik felhasználó a session-ban, akkor töltjük be a felhasználói adatokat
                AdminUserSecurityDetails userDetails = adminUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // A SecurityContext-ben beállítjuk az autentikációt
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println("doFilterInternal - A4");
        // Folytatjuk a láncot
        filterChain.doFilter(request, response);
    }
}
