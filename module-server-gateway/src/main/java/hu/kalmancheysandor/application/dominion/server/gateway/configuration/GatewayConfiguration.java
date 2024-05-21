package hu.kalmancheysandor.application.dominion.server.gateway.configuration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

@Configuration
public class GatewayConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("ai-1", r -> r.path("/ai-1/**").uri("lb://ai-1"))
            .route("ai-2", r -> r.path("/ai-2/**").uri("lb://ai-2"))
            .route("game", r -> r.path("/game/**").uri("lb://game"))





//            .route("web.chat.info2", r -> r.path("/web/chat/info").and().query("t")
//                .filters(f -> f.filter(printPathFilter("web.chat.info2 ?"))).uri("localhost:7002"))


            .route("web.chat.info2", r -> r.path("/web/chat/info**")
                .filters(f -> f.filter(printPathFilter("web.chat.info"))).uri("localhost:7002"))



            .route("web.chat", r -> r.path("/web/chat/**")
                .filters(f -> f.filter(printPathFilter("web.chat"))).uri("ws://localhost:7002"))




//            .route("web.chat.info", r -> r.path("/web/chat/info/**")
//                .filters(f -> f.filter(printPathFilter("web.chat.info **"))).uri("wss://localhost:7002"))



            .route("web.topic", r -> r.path("/web/topic/**")
                .filters(f -> f.filter(printPathFilter("web.topic"))).uri("ws://localhost:7002"))

            .route("web", r -> r.path("/web/**")
                .filters(f -> f.filter(printPathFilter("web"))).uri("lb://web"))


















//            .route("web.chat.info2", r -> r.path("/web/chat/info").and().query("t")
//                .filters(f -> f.filter(printPathFilter("web.chat.info2 ?"))).uri("localhost:7002"))
//
//            .route("web.chat", r -> r.path("/web/chat/**")
//                    .filters(f -> f.filter(printPathFilter("web.chatTTTT"))).uri("ws://localhost:7002"))
//
//
//
//
//            .route("web.chat.info", r -> r.path("/web/chat/info/**")
//                .filters(f -> f.filter(printPathFilter("web.chat.info **"))).uri("wss://localhost:7002"))
//
//
//
//            .route("web.topic", r -> r.path("/web/topic/**")
//                    .filters(f -> f.filter(printPathFilter("web.topic"))).uri("ws://localhost:7002"))
//
//            .route("web", r -> r.path("/web/**")
//                .filters(f -> f.filter(printPathFilter("web"))).uri("lb://web"))

//            .route("web", r -> r.path("/web/**").uri("lb://web"))
            //.route("web.chat", r -> r.path("/chat/**").uri("lb://web"))
            .build();




//
//
//
//        routes:
//        id: demo
//        uri: lb://demo
//        predicates:
//        - Path=/api/message/ws/any-socket/info**
//
//        id: demo
//        predicates:
//        - Path=/api/message/ws/any-socket/**
//         uri: lb:ws://demo



    }


    private static GatewayFilter rewritePathFilter(String sectionToReplace, String replaceWith) {
        return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            addOriginalRequestUrl(exchange, req.getURI());
            String path = req.getURI().getRawPath();

            String newPath = path.replaceAll(sectionToReplace, replaceWith);
            System.out.println("ROOOTING>[" + path + "]>>>[" + newPath + "]");

            ServerHttpRequest request = req.mutate().path(newPath).build();
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
            return chain.filter(exchange.mutate().request(request).build());
        };
    }


    private static GatewayFilter printPathFilter(String flag) {
        return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            addOriginalRequestUrl(exchange, req.getURI());
            String path = req.getURI().getRawPath();

            String newPath = path;
            System.out.println("ROOOTING[" + flag + "]:"+ path + "|" +req.getURI().getQuery());

            ServerHttpRequest request = req.mutate().path(newPath).build();
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}
