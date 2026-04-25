package hu.kalmancheysandor.applications.dominions.servers.gateway.filter;


import org.apache.http.io.SessionOutputBuffer;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        HttpHeaders headers = exchange.getRequest().getHeaders();

        for(Map.Entry<String,List<String>> entry :headers.entrySet()) {




            System.out.println("XXX:"+entry.getKey()+" : "+String.join(",", entry.getValue()));
        }



        return chain.filter(exchange);
    }


}


