package hu.kalmancheysandor.applications.dominion.server.web.configuration.feign;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FeignConfiguration {

//    @Bean
    public CustomErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
