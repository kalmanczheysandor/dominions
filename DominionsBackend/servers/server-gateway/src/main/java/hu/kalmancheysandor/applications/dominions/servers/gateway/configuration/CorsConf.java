package hu.kalmancheysandor.applications.dominions.servers.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConf extends CorsConfiguration {
    @Value("${app.urls.admin-site}")
    private String adminSiteUrl;

    @Value("${app.urls.game-site}")
    private String gameSiteUrl;

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // IMPORTANT: Required for cookies/sessions to work in cross-origin requests
        config.setAllowCredentials(true);

        // Only allow the specified domains to make requests to the gateway
        config.setAllowedOrigins(List.of(
                adminSiteUrl,
                gameSiteUrl
        ));
//        config.setAllowedOrigins( List.of( "http://localhost:8080" ) ); //Collections.singtonList(....);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Set-Cookie")); // Required for the browser to accept the Set-Cookie header in the response.


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // Apply the configuration to all REST endpoints


        // Extend the CORS configuration to WebSocket endpoints as well
        source.registerCorsConfiguration("/test/spring-boot-chat", config); // WebSocket endpoint
        source.registerCorsConfiguration("/test/app/chat", config);       // WebSocket endpoint


        return new CorsWebFilter(source);
    }
}