package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent;


import com.fasterxml.jackson.databind.ObjectMapper;

import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.common.controller.advice",
        "hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign",
        "hu.kalmancheysandor.applications.dominions.apis.server.common.component",
        "hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.advice",
        "hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent"
})
@EnableAsync
@EnableFeignClients
@EnableJpaRepositories(basePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository",
//        "hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository",
        "hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent"
})
@EntityScan(basePackages = {
//    "hu.kalmancheysandor.applications.dominions.apis.server.user.common.entity",
//    "hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity",
        "hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent"
})
public class LizAgentServer {
    public static void main(String[] args) {
        SpringApplication.run(LizAgentServer.class, args);
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

    @Bean
    public UUIDGenerator uuidGenerator() {
        return new UUIDGenerator();
    }


    private void aCallTest() {
//        INeuralAiEngine myTestEngine = new  LizAiEngine();
//        myTestEngine.trainIt();

//        LizNeuralNetwork network = new LizNeuralNetwork();
//        network.train(null, null);
//        network.


    }

}