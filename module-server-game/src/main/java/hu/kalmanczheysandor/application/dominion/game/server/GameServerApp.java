package hu.kalmanczheysandor.application.dominion.game.server;


import hu.kalmanczheysandor.application.dominion.game.common.GameEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GameServerApp {
    public static void main(String[] args) {
        SpringApplication.run(GameServerApp.class, args);
    }

//    @Bean
//    public GameEngine getGameEngine() {
//        return new GameEngine();
//    }
}