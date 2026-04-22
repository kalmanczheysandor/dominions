package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.agent;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.hugo.HugoAiAdvancedEngine;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.common.controller.advice",
        "hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign",
        "hu.kalmancheysandor.applications.dominions.apis.server.common.component",
        "hu.kalmancheysandor.applications.dominions.servers.ai.hugo"
})
@EnableAsync
@EnableFeignClients
public class HugoAgentServer {
    public static void main(String[] args) {
        SpringApplication.run(HugoAgentServer.class, args);
    }

    @Bean
    public IAiEngine getBasicEngine() {
        return new HugoAiAdvancedEngine();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
}