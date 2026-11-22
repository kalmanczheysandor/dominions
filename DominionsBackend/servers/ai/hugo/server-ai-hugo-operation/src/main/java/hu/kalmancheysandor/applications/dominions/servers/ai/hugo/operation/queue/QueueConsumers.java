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

}
