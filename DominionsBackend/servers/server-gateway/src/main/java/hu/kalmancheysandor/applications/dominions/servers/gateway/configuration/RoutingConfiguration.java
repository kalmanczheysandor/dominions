package hu.kalmancheysandor.applications.dominions.servers.gateway.configuration;

import hu.kalmancheysandor.applications.dominions.servers.gateway.filter.RFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("server-monitoring", r -> r
                        .path("/monitoring/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-eureka")))
                        .uri("lb://server-monitoring")
                )
                .route("server-eureka", r -> r
                        .path("/eureka/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-eureka")))
                        .uri("lb://server-eureka")
                )
                .route("server-auth", r -> r
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-auth")))
                        .uri("lb://server-auth")
                )
                .route("server-site-websocket", r -> r
                        .path("/site/spring-boot-chat/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new RFilter("site-ws"))
                        )
                        .uri("lb:ws://server-site")
                )
                .route("server-site-normal", r -> r
                        .path("/site/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-site-A-3")))
                        .uri("lb://server-site")
                )
                .route("server-admin-normal", r -> r
                        .path("/admin/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-admin-A-2")))
                        .uri("lb://server-admin")
                )
                .build();
    }
}
