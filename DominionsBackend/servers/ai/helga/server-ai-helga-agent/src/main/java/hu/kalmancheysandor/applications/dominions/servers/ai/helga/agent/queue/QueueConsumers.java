package hu.kalmancheysandor.applications.dominions.servers.ai.helga.agent.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue.AiHistoryQueueItem;
import hu.kalmancheysandor.applications.dominions.servers.ai.helga.agent.service.HelgaAgentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class QueueConsumers {

    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    public Consumer<AiHistoryQueueItem> queueHistoryBroadcast(HelgaAgentService service) {
        return service::queueHistoryRead;
    }


    //
//    @Bean
//    public Consumer<String> queueHistoryBroadcast(LizService lizService) {
//        return payload -> {
//            try {
//                AiHistoryQueueItem queueItem = objectMapper.readValue(payload, AiHistoryQueueItem.class);
//                lizService.queueHistoryRead(queueItem);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//    }

}
