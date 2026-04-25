package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QueueConsumers {

    @Autowired
    private ObjectMapper objectMapper;

//    @Bean
//    public Consumer<HugoNeuralConceptTaskProcessingQueueItem> queueExecutionTaskProcessingItemRead(HugoNeuralOrchestrationService service) {
//        System.out.println("THE BEAN");
//        return service::eventTaskProcessingQueueItemReceived;
//    }
//
//
//    @Bean
//    public Consumer<AiHistoryQueueItem> queueHistoryBroadcast(HugoNeuralOrchestrationService service) {
//        System.out.println("TTX");
//        return service::eventHistoryQueueItemReceived;
//    }


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
