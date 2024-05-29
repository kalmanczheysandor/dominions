package hu.kalmancheysandor.application.dominion.server.ai.liz;

import hu.kalmancheysandor.application.dominion.api.ai.otto.OttoAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.IAiEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiLizServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AiLizServerApp.class, args);
    }

    @Bean
    public IAiEngine getBasicEngine() {
        return new OttoAiEngine();
    }
}