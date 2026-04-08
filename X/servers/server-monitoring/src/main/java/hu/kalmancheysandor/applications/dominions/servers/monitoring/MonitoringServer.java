package hu.kalmancheysandor.applications.dominions.servers.monitoring;

import de.codecentric.boot.admin.server.config.AdminServerHazelcastAutoConfiguration;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication(exclude=AdminServerHazelcastAutoConfiguration.class)
public class MonitoringServer {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringServer.class, args);
    }
}