package hu.kalmancheysandor.applications.dominions.servers.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.function.Consumer;

@RestController
@RequestMapping("/test/queue")
@Slf4j
public class QueueController {


    @Autowired
    private StreamBridge streamBridge;


    @GetMapping("/{value}")
    @ResponseStatus(HttpStatus.OK)
    public void values(@PathVariable String value) {
        log.error("PRODUCER: {} \n", value);
        streamBridge.send("test-out-0","macska a hazban");
    }

    @Bean
    public Consumer<String> myTest() {
        return content -> {
            log.error("CONSUMER-MY TEST: {} \n", content);
        };
    }


}
