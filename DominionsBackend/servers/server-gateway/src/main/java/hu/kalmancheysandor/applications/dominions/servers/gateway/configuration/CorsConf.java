package hu.kalmancheysandor.applications.dominions.servers.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConf extends CorsConfiguration {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // FONTOS: kell, hogy a cookie/session működjön cross-origin esetén
        config.setAllowCredentials(true);

        // Csak az engedélyezett domainek, amik ténylegesen hívhatják a gateway-t
        config.setAllowedOrigins( List.of(
                "http://localhost:8080",
                "http://localhost:8081",
                "https://game.dominions.hu",
                "https://admin.dominions.hu"
                ) );
//        config.setAllowedOrigins( List.of( "http://localhost:8080" ) ); //Collections.singtonList(....);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Set-Cookie")); // ez a kulcs ahhoz, hogy a böngésző elfogadja a Set-Cookie fejlécet a válaszban.


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // Alkalmazzuk minden REST végpontra is




        // WebSocketekhez is kiterjesztjük a CORS beállítást
        source.registerCorsConfiguration("/spring-boot-chat", config); // WebSocket endpoint
        source.registerCorsConfiguration("/app/chat", config);       // WebSocket endpoint


        return new CorsWebFilter(source);
    }
}
