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
                .route("server-auth", r -> r
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-auth")))
                        .uri("lb://server-auth")
                )

                .route("server-site-websocket-handshake", r -> r
                        .path("/site/ws/connect/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-site-A-1")))
                        .uri("lb://server-site")
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



//                .route("server-game", r -> r.path("/game/**").filters(f -> f.stripPrefix(1).filter(new RFilter("server-Game-1"))).uri("lb://server-game"))
//                .route("server-ai-otto", r -> r.path("/ai/otto/**").filters(f -> f.stripPrefix(2).filter(new RFilter("server-ai-otto-1"))).uri("lb://server-ai-otto"))

//                .route("server-test-websocket-handshake", r -> r
//                        .path("/test/spring-boot-chat/**")
//                        .filters(f -> f
//                                .stripPrefix(1).
//                                filter(new RFilter("server-test-A-1"))
//                        )
//                        .uri("lb://server-test"))
//
//
//                .route("server-test-websocket-handshake", r -> r
//                        .path("/test/**")
//                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-test-A-3")))
//                        .uri("lb://server-test"))

                .build();
    }
}
