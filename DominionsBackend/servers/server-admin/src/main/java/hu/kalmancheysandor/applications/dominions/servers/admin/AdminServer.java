package hu.kalmancheysandor.applications.dominions.servers.admin;



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
        "hu.kalmancheysandor.applications.dominions.apis.server.user.common",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.admin",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.site",
//        "hu.kalmancheysandor.applications.dominions.apis.server.game.common",
        "hu.kalmancheysandor.applications.dominions.servers.admin"
})
@EnableAsync
@EnableFeignClients(basePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.game.common.proxy",
        "hu.kalmancheysandor.applications.dominions.apis.server.ai.common.proxy",
        "hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy",
})
@EnableJpaRepositories(basePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.user.common.repository",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository",
        "hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository",
        "hu.kalmancheysandor.applications.dominions.servers.admin"
})
@EntityScan(basePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.user.common.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity",
        "hu.kalmancheysandor.applications.dominions.servers.admin"
})
public class AdminServer {
    public static void main(String[] args) {
        SpringApplication.run(AdminServer.class, args);
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

//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public FileHandler imageHandler() {
//        return new FileHandler();
//    }

}