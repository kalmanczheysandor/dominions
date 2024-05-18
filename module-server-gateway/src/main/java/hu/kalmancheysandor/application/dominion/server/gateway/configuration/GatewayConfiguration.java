package hu.kalmancheysandor.application.dominion.server.gateway.configuration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

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
                      .route("web", r -> r.path("/web/**").uri("lb://web"))
                      .build();
    }


//	@Bean
//	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route(p -> p
//						.path("/get")
//						.filters(f -> f
//								.addRequestHeader("MyHeader", "MyURI")
//								.addRequestParameter("Param", "MyValue"))
//						.uri("http://httpbin.org:80"))
//				.route(p -> p.path("/currency-exchange/**")
//						.uri("lb://currency-exchange"))
//				.route(p -> p.path("/currency-conversion/**")
//						.uri("lb://currency-conversion"))
//				.route(p -> p.path("/currency-conversion-feign/**")
//						.uri("lb://currency-conversion"))
//				.route(p -> p.path("/currency-conversion-new/**")
//						.filters(f -> f.rewritePath(
//								"/currency-conversion-new/(?<segment>.*)",
//								"/currency-conversion-feign/${segment}"))
//						.uri("lb://currency-conversion"))
//				.build();
//	}

//    @Bean
//    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
//        return builder.routes()
//                      .route("ai1", r -> r.path("/ai1/**").uri("lb://ai1"))
//
////                      .route("web_css", r -> r.path("/web/css/custom.css").uri("http://atv.hu")) >>> https://www.atv.hu/web/css/custom.css
////                      .route("web_css", r -> r.path("/web/css/**").uri("http://localhost:7002")) //>>> https://www.atv.hu/web/css/custom.css
////
////                      .route("web_css", r -> r.path("/css/**").uri("lb://web"))
//                      .route("web_js", r -> r.path("/js/**").uri("lb://web"))
//                      .route("web_image", r -> r.path("/image/**").uri("lb://web"))
//                      .route("web_font", r -> r.path("/font/**").uri("lb://web"))
//                      .route("web.logout", r -> r.path("/logout").uri("lb://web/web"))
//
//                      .route("web_css", r -> r.path("/css/**")
//                                              .filters(f -> f.filter(rewritePathFilter("/css", "/css")))
//                                              .uri("lb://web"))
//                      .route("web", r -> r.path("/web/**").uri("lb://web"))
//
//
////                      .route("web_css", r -> r.path("/web/css/**")
////                                                     .filters(f -> f.filter((exchange, chain) -> {
////                                                         String originalUri = exchange.getRequest().getURI().toString();
////                                                         String redirectUri = originalUri.replace("/web/css", "/css");
////                                                         exchange.getResponse().setStatusCode(HttpStatus.FOUND);
////                                                         exchange.getResponse().getHeaders().setLocation(URI.create(redirectUri));
////                                                         return chain.filter(exchange);
////                                                     }))
////                                              .uri("http://localhost:7002"))
//
//
//                      .build();
//
//
////        return builder.routes()
////                      .route("web", r -> r.path("/web/**").uri("lb://web"))
////                      .build();
//
//        //                      .route("microservice_a_css", r -> r.path("/microserviceA/css/**")
////                                                         .uri("http://microservice-a.example.com/static/css/"))
////            .route("microservice_b_css", r -> r.path("/microserviceB/css/**")
////                                               .uri("http://microservice-b.example.com/static/css/"))
////
//
//    }
//

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
}
