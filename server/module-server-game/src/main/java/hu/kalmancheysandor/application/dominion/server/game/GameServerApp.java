package hu.kalmancheysandor.application.dominion.server.game;


import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class GameServerApp {
    public static void main(String[] args) {
        SpringApplication.run(GameServerApp.class, args);
    }

    @Bean
    public GameEngine gameEngine() {
        return new GameEngine();
    }
}