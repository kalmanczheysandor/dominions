package hu.kalmancheysandor.applications.dominions.servers.auth.configuration.security;


import hu.kalmancheysandor.applications.dominions.servers.auth.configuration.security.admin.AdminAuthenticationFilter;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.AdminUserDetailsService;
import hu.kalmancheysandor.applications.dominions.servers.auth.configuration.security.site.SiteAuthenticationFilter;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.SiteUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Autowired
    private SiteUserDetailsService siteUserDetailsService;

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http, SecurityContextRepository contextRepository) throws Exception {
        try {
            AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter("/admin/login", generateAdminAuthenticationManager(), adminUserDetailsService);

            http
                    .securityMatcher("/admin/**") // csak site endpointok
                    .csrf(csrf -> csrf.disable())// CSRF-t kikapcsolod, ami rendben van REST-nél
                    .cors(Customizer.withDefaults())// CORS beállításaid helyesek, itt nincs gond
                    .securityContext(context -> context
                            .securityContextRepository(contextRepository)
                    )// Biztosítja a `SecurityContext` mentését/visszaállítását
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    )// Használj mindig új session-t
                    .authorizeHttpRequests(request -> request
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    .requestMatchers("/spring-boot-chat/**").permitAll()
                                    .requestMatchers("/test/**").permitAll()
                                    .requestMatchers("/app/**").permitAll()
//                        .requestMatchers("/admin/login").permitAll()
                                    .requestMatchers("/admin/login").permitAll()
                                    .requestMatchers("/data/test/**").permitAll()
                                    .requestMatchers("/data/**").authenticated()
                                    .requestMatchers("/main").authenticated()
                                    .requestMatchers("/main/**").authenticated()
                                    .requestMatchers("/css/**").permitAll()
                                    .requestMatchers("/js/**").permitAll()
                                    .requestMatchers("/font/**").permitAll()
                                    .requestMatchers("/image/**").permitAll()
                                    .requestMatchers("/login").permitAll()
                    )
                    .formLogin(f -> f.disable())
//                  .addFilterBefore(new AdminSessionCookieFilter(), SessionManagementFilter.class)   // Ezzel biztositom, hogy az alapertelmezett cooki kezeles elott a sajatom kezelje
                    .addFilterBefore(adminAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout(logout -> logout
                            .logoutUrl("/admin/logout")
                            .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout", "POST"))
                            .logoutSuccessHandler((request, response, authentication) -> {
                                if (authentication != null) {
                                    System.out.println("User logged out: " + authentication.getName());
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
                            .deleteCookies("ADMINSESSION")// Session cookie delete
                            .clearAuthentication(true)  // Delete securityContext to avoid usage of alive object stored in memory
                            .invalidateHttpSession(true)//Invalidate session
                    );

            return http.build();
        } catch (Exception exp) {
            System.out.println(exp);
        }
        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain siteFilterChain(HttpSecurity http, SecurityContextRepository contextRepository) throws Exception {

        SiteAuthenticationFilter siteAuthenticationFilter = new SiteAuthenticationFilter("/site/login", generateSiteAuthenticationManager(), siteUserDetailsService);
        http
                .securityMatcher("/site/**") // csak site endpointok
                .csrf(csrf -> csrf.disable())// CSRF-t kikapcsolod, ami rendben van REST-nél
                .cors(Customizer.withDefaults())// CORS beállításaid helyesek, itt nincs gond
                .securityContext(context -> context
                        .securityContextRepository(contextRepository)
                )// Biztosítja a `SecurityContext` mentését/visszaállítását
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )// Használj mindig új session-t
                .authorizeHttpRequests(request -> request
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/spring-boot-chat/**").permitAll()
                                .requestMatchers("/test/**").permitAll()
                                .requestMatchers("/app/**").permitAll()
//                        .requestMatchers("/admin/login").permitAll()
                                .requestMatchers("/site/login").permitAll()
                                .requestMatchers("/data/test/**").permitAll()
                                .requestMatchers("/data/**").authenticated()
                                .requestMatchers("/main").authenticated()
                                .requestMatchers("/main/**").authenticated()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .requestMatchers("/font/**").permitAll()
                                .requestMatchers("/image/**").permitAll()
                                .requestMatchers("/login").permitAll()
                )
                .formLogin(f -> f.disable())
//                .addFilterBefore(new SiteSessionCookieFilter(), SessionManagementFilter.class)   // Ezzel biztositom, hogy az alapertelmezett cooki kezeles elott a sajatom kezelje
                .addFilterBefore(siteAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/site/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/site/logout", "POST"))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                System.out.println("User logged out: " + authentication.getName());
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
                        .deleteCookies("SITESESSION")// Session cookie delete
                        .clearAuthentication(true)  // Delete securityContext to avoid usage of alive object stored in memory
                        .invalidateHttpSession(true)//Invalidate session
                );

        return http.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:8080",
                                "https://localhost:8080",
                                "http://localhost:8081",
                                "https://localhost:8081",
                                "https://game.dominions.hu",
                                "https://admin.dominions.hu"
                        ) // A frontend URL
//                    .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                        .allowCredentials(true) // Engedélyezi a session cookie-kat
                        .exposedHeaders("Set-Cookie"); // EZ FONTOS!
            }
        };
    }

    public AuthenticationManager generateSiteAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(siteUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }

    public AuthenticationManager generateAdminAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return new CentralHttpSessionIdResolver();
//    }

}
