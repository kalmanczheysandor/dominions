package hu.kalmancheysandor.application.dominion.server.ai.otto;

import hu.kalmancheysandor.application.dominion.api.ai.otto.OttoAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.IAiEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OttoAiServerApp {
    public static void main(String[] args) {
        SpringApplication.run(OttoAiServerApp.class, args);
    }

    @Bean
    public IAiEngine getBasicEngine() {
        return new OttoAiEngine();
    }
}