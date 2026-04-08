package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;

import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.authentication.AdminAuthenticationFilter;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.authentication.SiteAuthenticationFilter;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {
    @Autowired
    //@Lazy
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;


    @Autowired
    @Lazy
    private SecurityContextRepository contextRepository;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter(authenticationManager);
        SiteAuthenticationFilter siteAuthenticationFilter = new SiteAuthenticationFilter(authenticationManager);

        http
            .csrf(csrf -> csrf.disable()) // CSRF-t kikapcsolod, ami rendben van REST-nél
            .cors(Customizer.withDefaults()) // CORS beállításaid helyesek, itt nincs gond
            .securityContext(context -> context
                .securityContextRepository(contextRepository)) // Biztosítja a `SecurityContext` mentését/visszaállítását
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Használj mindig új session-t
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers("/spring-boot-chat/**").permitAll()    // websocket
                .requestMatchers("/test/**").permitAll()
                .requestMatchers("/app/**").permitAll()
                .requestMatchers("/data/auth/login").permitAll()
//                .requestMatchers("/data/auth/login2").permitAll()
                .requestMatchers("/data/**").authenticated()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/main").authenticated() // Test access with any authenticated user
                .requestMatchers("/main/**").authenticated() // Test access with any authenticated user
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/font/**").permitAll()
                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/login").permitAll()
            )
            .formLogin(f -> f.disable())

//            .formLogin(f->f
//                .failureHandler(myAuthenticationFailureHandler)
//                .loginProcessingUrl("/data/auth/login")
//                .successHandler(myAuthenticationSuccessHandler)
//                .loginPage("/login").permitAll()
//            )

            .addFilterBefore(siteAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(adminAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//            .addFilterBefore(new OncePerRequestFilter() {
//                @Override
//                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//                    System.out.println("Incoming request: " + request.getRequestURI());
//                    System.out.println("Session ID: " + request.getSession().getId());
//
//                    // Tároljuk a SecurityContext-ot a session-ban, ha szükséges
//                    HttpSession session = request.getSession(false); // Get the existing session if it exists
//                    if (session != null) {
//                        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
//                        if (context != null) {
//                            SecurityContextHolder.setContext(context);
//                        }
//                    }
//
//                    System.out.println("22222SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
//
//                    // Continue with the filter chain
//                    filterChain.doFilter(request, response);  // Fontos lépés: biztosítja, hogy a lánc folytatódjon
//
//                }
//            }, CustomAuthenticationFilter.class)  // Itt adjuk hozzá a logoló szűrőt


            .logout(logout -> logout
                .logoutUrl("/data/auth/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/data/auth/logout", "POST")) // Only for post calls
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        System.out.println("User Looged out: " + authentication.getName());
                    }

                    // Session invalidation
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate();
                    }

                    // SecurityContext delete
                    SecurityContextHolder.clearContext();

                    // HTTP response
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().flush();
                })
                .deleteCookies("JSESSIONID") // Session cookie delete
                .invalidateHttpSession(true) //Invalidate session
            )

        ;

        return http.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:8080") // A frontend URL
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                    .allowCredentials(true); // Engedélyezi a session cookie-kat
            }
        };
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        System.out.println("-----AuthenticationManager-------");
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager am = authenticationManagerBuilder.build();

        System.out.println(">AM:" + am.toString());
        return am;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
