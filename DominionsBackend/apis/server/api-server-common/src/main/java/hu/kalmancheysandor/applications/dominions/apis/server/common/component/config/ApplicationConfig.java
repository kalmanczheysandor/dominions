package hu.kalmancheysandor.applications.dominions.apis.server.common.component.config;

import hu.kalmancheysandor.applications.dominions.apis.util.configuration.TApplicationConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@RefreshScope
public class ApplicationConfig extends TApplicationConfiguration {

}
