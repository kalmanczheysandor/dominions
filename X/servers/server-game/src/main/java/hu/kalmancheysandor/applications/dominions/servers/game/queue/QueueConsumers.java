package hu.kalmancheysandor.applications.dominions.servers.game.queue;

import hu.kalmancheysandor.applications.dominions.servers.game.service.game.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class QueueConsumers {

    @Bean
    public Consumer<HistoryQueueItem> queueHistory(GameService gameService) {
        return gameService::queueHistoryRead;
    }
}
