package hu.kalmancheysandor.application.dominion.server.ai.player1;

import hu.kalmancheysandor.application.dominion.api.ai.otto.OttoAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiPlayer1ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AiPlayer1ServerApp.class, args);
    }

    @Bean
    public AiEngine getBasicEngine() {
        return new OttoAiEngine();
    }
}