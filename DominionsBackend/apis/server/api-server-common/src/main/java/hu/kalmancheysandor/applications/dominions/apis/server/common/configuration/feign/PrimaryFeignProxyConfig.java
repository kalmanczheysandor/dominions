package hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign;


import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrimaryFeignProxyConfig {

    @Value("${app.global.secret-application-key}")
    private String apiKey;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RequestInterceptor serviceKeyInterceptor() {
        return template -> {
            template.header("X-Service-Key", apiKey);
            template.header("X-Service-Name", applicationName);
        };
    }
}
