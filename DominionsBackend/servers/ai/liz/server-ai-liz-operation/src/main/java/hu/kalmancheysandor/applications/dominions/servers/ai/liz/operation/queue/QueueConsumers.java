package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
//import hu.kalmancheysandor.applications.dominions.servers.ai.liz.training.service.LizTrainingService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue.AiHistoryQueueItem;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.queue.LizNeuralConceptTaskProcessingQueueItem;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural.LizNeuralOrchestrationService;
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
    public Consumer<LizNeuralConceptTaskProcessingQueueItem> queueExecutionTaskProcessingItemRead(LizNeuralOrchestrationService service) {
        System.out.println("THE BEAN");
        return service::eventTaskProcessingQueueItemReceived;
    }


    @Bean
    public Consumer<AiHistoryQueueItem> queueHistoryBroadcast(LizNeuralOrchestrationService service) {
        System.out.println("TTX");
        return service::eventHistoryQueueItemReceived;
    }



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
