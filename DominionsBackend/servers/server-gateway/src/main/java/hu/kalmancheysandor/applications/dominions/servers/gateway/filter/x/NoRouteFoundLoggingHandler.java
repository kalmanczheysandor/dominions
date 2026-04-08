package hu.kalmancheysandor.applications.dominions.servers.gateway.filter.x;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
public class NoRouteFoundLoggingHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        if (ex instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            String url = exchange.getRequest().getURI().toString();
            System.out.println("NO ROUTE FOUND for URL: " + url);
        }

        return Mono.error(ex); // tovább engedjük a hibát
    }
}

