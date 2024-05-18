package hu.kalmancheysandor.application.dominion.server.game;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class GameServerApp {
    public static void main(String[] args) {
        SpringApplication.run(GameServerApp.class, args);
    }
}