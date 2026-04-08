package hu.kalmancheysandor.applications.dominions.servers.ai.helga;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.helga.HelgaAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
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
    "hu.kalmancheysandor.applications.dominions.servers.ai.helga"
})
@EnableAsync
@EnableFeignClients
@EnableJpaRepositories(basePackages = {
    "hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository",
    "hu.kalmancheysandor.applications.dominions.servers.ai.helga"
})
@EntityScan(basePackages = {
    "hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity",
    "hu.kalmancheysandor.applications.dominions.servers.ai.helga"
})
public class HelgaAiServer {
    public static void main(String[] args) {
        SpringApplication.run(HelgaAiServer.class, args);
    }

    @Bean
    public INeuralAiEngine getBasicEngine() {
        return new HelgaAiEngine();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
}