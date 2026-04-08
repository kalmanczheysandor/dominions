package hu.kalmancheysandor.applications.dominions.servers.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
public class ResponseLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ResponseLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("[FilterChain]:+ResponseLoggingFilter");
        return chain.filter(exchange)
            .doOnSuccess(aVoid -> {
                System.out.println("Do on success");
                HttpHeaders headers = exchange.getResponse().getHeaders();
                log.info("!!!!!!!!Response Headers: {}", headers);
            })
            .doOnTerminate(() -> {
                System.out.println("Do on terminate");
                HttpHeaders headers = exchange.getResponse().getHeaders();
                log.info("!!!!!!!!!Response Headers: {}", headers);
            })
            .doOnError(aVoid -> {
                System.out.println("Do on Error");
                HttpHeaders headers = exchange.getResponse().getHeaders();
                log.info("!!!!!!!!Response Headers: {}", headers);
            });
    }

    @Override
    public int getOrder() {
        return -1; // Alacsonyabb szám = korábbi végrehajtás
    }
}

