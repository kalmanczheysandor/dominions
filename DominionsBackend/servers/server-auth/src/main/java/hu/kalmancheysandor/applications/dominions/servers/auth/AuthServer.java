package hu.kalmancheysandor.applications.dominions.servers.auth;


import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;

@SpringBootApplication(scanBasePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.common.controller.advice",
        "hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign",
        "hu.kalmancheysandor.applications.dominions.apis.server.common.component",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.common",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.admin",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.site",
        "hu.kalmancheysandor.applications.dominions.servers.auth"
})
@EnableJpaRepositories(basePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.user.common.repository",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository",
        "hu.kalmancheysandor.applications.dominions.servers.auth"
})
@EntityScan(basePackages = {
        "hu.kalmancheysandor.applications.dominions.apis.server.user.common.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity",
        "hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity",
        "hu.kalmancheysandor.applications.dominions.servers.auth"
})
public class AuthServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthServer.class, args);
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

}
