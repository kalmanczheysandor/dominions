package hu.kalmancheysandor.application.dominion.server.ai.player2;

import hu.kalmancheysandor.application.dominion.api.ai.otto.PseudoAi2Engine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiPlayer2ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AiPlayer2ServerApp.class, args);
    }

    @Bean
    public AiEngine getBasicEngine() {
        return new PseudoAi2Engine();
    }
}