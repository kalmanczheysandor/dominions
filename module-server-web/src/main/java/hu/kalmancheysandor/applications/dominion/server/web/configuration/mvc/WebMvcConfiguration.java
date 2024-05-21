package hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//public class WebMvcConfiguration implements WebMvcConfigurer {
@Configuration
public class WebMvcConfiguration {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor());
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*") // Allow all origins
            .allowedMethods("GET", "POST", "PUT", "DELETE","UPGRADE") // Allow specific HTTP methods
            .allowedHeaders("*"); // Allow all headers
        System.out.println("CORS IS DONE");
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*") // Allow all origins
                    .allowedMethods("GET", "POST", "PUT", "DELETE","UPGRADE") // Allow specific HTTP methods
                    .allowedHeaders("*"); // Allow all headers
                System.out.println("CORS IS DONE");
            }
        };
    }


}
