package hu.kalmancheysandor.applications.dominions.servers.gateway.filter;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        HttpHeaders headers = exchange.getRequest().getHeaders();

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            // System.out.println("XXX:"+entry.getKey()+" : "+String.join(",", entry.getValue()));
        }

        return chain.filter(exchange);
    }

    private String determineRequestOrigin(ServerWebExchange exchange) {
        String origin = exchange.getRequest().getHeaders().getFirst("Origin");
        String referer = exchange.getRequest().getHeaders().getFirst("Referer");

        if (referer != null && !referer.equals("")) {
            return referer;

        } else if (origin != null && !origin.equals("")) {
            return origin;

        }

        return null;
    }

}


