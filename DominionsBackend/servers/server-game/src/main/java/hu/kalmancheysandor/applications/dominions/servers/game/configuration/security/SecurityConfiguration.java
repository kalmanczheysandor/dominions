package hu.kalmancheysandor.applications.dominions.servers.game.configuration.security;


import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.SecurityApiKeyFilter;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.SecurityContextDebugFilter;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.TAccessDenyHandler;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.TAuthenticationEntryPoint;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.SiteUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Value("${app.urls.admin-site}")
    private String adminSiteUrl;

    @Value("${app.urls.game-site}")
    private String gameSiteUrl;

    @Value("${app.global.secret-application-key}")
    private String secretApiKey;


    @Autowired
    @Lazy
    private SecurityContextRepository contextRepository;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .securityContext(context -> context.securityContextRepository(contextRepository)) // It provides saving and loading of SecurityContext
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterAfter(new SecurityContextDebugFilter(), SecurityContextHolderFilter.class)
                .addFilterBefore(new SecurityApiKeyFilter(secretApiKey), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new TAuthenticationEntryPoint())
                        .accessDeniedHandler(new TAccessDenyHandler())
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(f -> f.disable());

        return http.build();
    }

//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins(
//                                adminSiteUrl,
//                                gameSiteUrl
//                        )
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
//                        .allowedHeaders("*")
//                        .allowCredentials(true) // Engedélyezi a session cookie-kat
//                        .exposedHeaders("Set-Cookie"); // EZ FONTOS!
//            }
//        };
//    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
