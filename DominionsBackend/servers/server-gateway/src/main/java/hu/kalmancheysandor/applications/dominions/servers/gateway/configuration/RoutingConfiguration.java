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
//                .route("server-api", r -> r
//                .path("/api/auth/**")
//                .filters(f -> f.stripPrefix(2).filter(new RFilter("server-auth")))
//                .uri("lb://server-auth")
//        )

                .route("server-auth", r -> r
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-auth")))
                        .uri("lb://server-auth")
                )

//
//                .route("server-site-websocket", r -> r
//                        .path("/site/spring-boot-chat/**")
//                        .filters(f -> f
//                                .stripPrefix(1)              // levágja a /site-t
//                                .filter(new RFilter("site-ws"))
//                        )
//                        .uri("lb:ws://server-site")      // ← KULCSFONTOSSÁGÚ!
//                )


//                .route("server-site-socket", r -> r
//                        .path("/api/site/**")
//                        .filters(f -> f
//                                .stripPrefix(2)
//                                .filter((exchange, chain) -> {
//                                    System.out.println(">>> AFTERrrr STRIP: " + exchange.getRequest().getURI());
//                                    return chain.filter(exchange);
//                                })
//                                .filter(new RFilter("server-site-socket"))
//                        )
//                        .uri("lb://server-site")
//                )


                .route("server-site-ws", r -> r
                        .path("/site/spring-boot-chat/*/*/websocket")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb:ws://server-site")
                )

                .route("server-site-http", r -> r
                        .path("/site/spring-boot-chat/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://server-site")
                )



//                .route("server-site-socket", r -> r
//                        .path("/site/spring-boot-chat/**")
//                        .filters(f -> f
//                                .stripPrefix(1)
//                                .filter((exchange, chain) -> {
//                                    System.out.println(">>> AFTER STRIP: " + exchange.getRequest().getURI());
//                                    return chain.filter(exchange);
//                                })
//                                .filter(new RFilter("server-site-socket"))
//                        )
//                        .uri("lb:ws://server-site")
//                )
//                .route("server-site-websocket-handshake", r -> r
//                        .path("/site/ws/connect/**")
//                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-site-A-1")))
//                        .uri("lb://server-site")
//                )

                .route("server-site-normal", r -> r
                        .path("/site/**")
                        .filters(f -> f.stripPrefix(1).filter(new RFilter("server-site-normal")))
                        .uri("lb://server-site")
                )


//
//                .route("server-ai-liz-operation", r -> r
//                        .path("/admin/ai/liz/operation/**")
//                        .filters(f -> f.stripPrefix(3).filter(new RFilter("server-ai-liz-operation-1")))
//                        .uri("lb://server-ai-liz-operation")
//                )
//                .route("server-ai-liz-agent", r -> r
//                        .path("/admin/ai/liz/agent/**")
//                        .filters(f -> f.stripPrefix(3).filter(new RFilter("server-ai-liz-agent-1")))
//                        .uri("lb://server-ai-liz-agent")
//                )


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
