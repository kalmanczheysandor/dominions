package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

    @Autowired
    @Lazy
    private SecurityContextRepository contextRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF-t kikapcsolod, ami rendben van REST-nél
                .cors(cors -> cors.disable())
                .securityContext(context -> context
                        .securityContextRepository(contextRepository)) // Biztosítja a `SecurityContext` mentését/visszaállítását
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Használj mindig új session-t
                )
                .addFilterAfter(
                        new SecurityContextDebugFilter(),
                        org.springframework.security.web.context.SecurityContextHolderFilter.class
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/spring-boot-chat/**").permitAll()    // websocket
                        .requestMatchers("/site/spring-boot-chat/**").permitAll()    // websocket
                        .requestMatchers("/app/**").permitAll()//websocket
                        .requestMatchers("/site/app/**").permitAll()//websocket
                        .requestMatchers("/site/**").permitAll()
                        .requestMatchers("/data/**").permitAll()

                )
                .formLogin(f -> f.disable())
        ;

        return http.build();
    }



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            adminSiteUrl
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                        .allowedHeaders("*")
                        .allowCredentials(true) // Engedélyezi a session cookie-kat
                        .exposedHeaders("Set-Cookie"); // EZ FONTOS!
            }
        };
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
