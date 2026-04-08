package hu.kalmancheysandor.applications.dominions.servers.gateway.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

//@Configuration
//@EnableWebFlux
public class CorsGlobalConfiguration implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
//        // Engedélyez minden kérést, bármilyen origin-ről, bármilyen metódussal és minden fejléccel
//        corsRegistry.addMapping("/**")
//            .allowedOrigins("http://localhost:8080","http://localhost:15000","http://localhost:15010") // Minden origin engedélyezése
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Minden HTTP metódus engedélyezése (GET, POST, PUT, DELETE, stb.)
//            .allowedHeaders("*") // Minden fejléc engedélyezése
//
//            .allowCredentials(true); // Ha szeretnéd, engedélyezheted a cookie-kat és hitelesítési adatokat
//
    }
}
