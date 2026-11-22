package hu.kalmancheysandor.applications.dominions.servers.mailing.configuration.security;


import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.SecurityApiKeyFilter;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.SecurityContextDebugFilter;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.TAccessDenyHandler;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.security.TAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;


@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Value("${app.urls.admin-site}")
    private String adminSiteUrl;

    @Value("${app.urls.game-site}")
    private String gameSiteUrl;


    @Value("${app.global.secret-application-key}")
    private String secretApiKey;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}