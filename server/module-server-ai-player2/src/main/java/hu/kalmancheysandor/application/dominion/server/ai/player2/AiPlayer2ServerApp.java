package hu.kalmancheysandor.application.dominion.server.ai.player2;

import hu.kalmancheysandor.application.dominion.api.ai.basic.Basic1Engine;
import hu.kalmancheysandor.application.dominion.api.ai.basic.Basic2Engine;
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
        return new Basic2Engine();
    }
}