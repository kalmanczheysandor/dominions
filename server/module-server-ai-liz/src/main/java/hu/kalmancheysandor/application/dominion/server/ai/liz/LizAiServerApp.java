package hu.kalmancheysandor.application.dominion.server.ai.liz;

import hu.kalmancheysandor.application.dominion.api.ai.common.neural.INeuralAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.liz.LizAiEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LizAiServerApp {
    public static void main(String[] args) {
        SpringApplication.run(LizAiServerApp.class, args);
    }

    @Bean
    public INeuralAiEngine getBasicEngine() {
        return new LizAiEngine();
    }
}