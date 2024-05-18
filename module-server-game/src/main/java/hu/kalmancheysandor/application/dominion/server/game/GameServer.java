package hu.kalmancheysandor.application.dominion.server.game;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameServer {
    public static void main(String[] args) {
        SpringApplication.run(GameServer.class, args);
    }

//    @Bean
//    public GameEngine getGameEngine() {
//        return new GameEngine();
//    }
}