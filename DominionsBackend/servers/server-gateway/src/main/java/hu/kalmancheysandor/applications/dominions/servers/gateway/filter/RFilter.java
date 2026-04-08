package hu.kalmancheysandor.applications.dominions.servers.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
public class RFilter implements GatewayFilter {

    private String routeName;

    public RFilter(String routeName) {
        this.routeName = routeName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("[FilterChain]: RFilter");
        String routeId = exchange.getAttributeOrDefault("spring.cloud.gateway.route_id", "UNKNOWN");
//        System.out.println("Route hit: " + routeId);  // Log the route that is being hit
        System.out.println("Route hit: " + routeName);  // Log the route that is being hit
        return chain.filter(exchange);
    }
}
