package hu.kalmancheysandor.applications.dominions.servers.test;

//import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "hu.kalmancheysandor.applications.dominions.apis.server.common.repository")
@EntityScan(basePackages = "hu.kalmancheysandor.applications.dominions.apis.server.common.entity")
public class TestServer {
    public static void main(String[] args) {
        SpringApplication.run(TestServer.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

//    @Bean
//    public UUIDGenerator uuidGenerator() {
//        return new UUIDGenerator();
//    }

}