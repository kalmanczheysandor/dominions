package hu.kalmancheysandor.applications.dominions.servers.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

    private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("Path of the request received -> {}"+exchange.getRequest().getPath());
//        return chain.filter(exchange);
//    }



//    public Mono<Void> filter2(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String originalUrl = exchange.getRequest().getURI().toString();
//        String transformedUrl = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR) != null
//            ? exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR).toString()
//            : "UNKNOWN";
//
////        logger.info("Original request URL: {}", originalUrl);
////        logger.info("Transformed request URL: {}", transformedUrl);
//
//        System.out.println("ROUTING: "+originalUrl+" >>> "+transformedUrl);
//
//        return chain.filter(exchange);
//    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("[FilterChain]: LoggingFilter");
//        // Finding session id
//        String sessionId = exchange.getRequest().getCookies().getFirst("SESSION") != null
//            ? exchange.getRequest().getCookies().getFirst("SESSION").getValue()
//            : null;
//
//        // Injecting session id
//        if (sessionId != null) {
//            exchange.getRequest().mutate()
//                .header("X-Session-ID", sessionId)
//                .build();
//            System.out.println("!!!!X-Session-ID"+sessionId);
//        }

        // print out routing
        String originalUrl = exchange.getRequest().getURI().toString();
        String transformedUrl = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR) != null
            ? exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR).toString()
            : "UNKNOWN";
        System.out.println("ROUTING: "+originalUrl+" >>> "+transformedUrl);

        return chain.filter(exchange);
    }

}
