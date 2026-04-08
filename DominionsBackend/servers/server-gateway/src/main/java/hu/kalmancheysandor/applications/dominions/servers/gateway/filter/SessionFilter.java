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


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        HttpHeaders headers = exchange.getRequest().getHeaders();

        for(Map.Entry<String,List<String>> entry :headers.entrySet()) {




            System.out.println("XXX:"+entry.getKey()+" : "+String.join(",", entry.getValue()));
        }



//        // 1) Ha a böngésző küld SESSION cookie-t, biztosítjuk, hogy a backend REQUEST headerében
//        // szerepeljen "Cookie: SESSION=<value>" (normalize)
//        String sessionValue = Optional.ofNullable(exchange.getRequest().getCookies().getFirst("SESSION"))
//                .map(HttpCookie::getValue)
//                .orElse(null);
//
//        ServerHttpRequest mutatedRequest = exchange.getRequest();
//        if (sessionValue != null) {
//            // Ha már van Cookie fejléc, felülírjuk csak a SESSION részt, vagy hozzáadjuk, ha nincs
//            List<String> existingCookieHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.COOKIE);
//            String cookieHeader = "SESSION=" + sessionValue;
//
//            // opcionális: megtartjuk más cookie-kat, ha szükséges:
//            // String merged = existingCookieHeaders.stream().collect(join(","));
//            // majd összeállítod úgy, hogy SESSION=... legyen benne
//            mutatedRequest = exchange.getRequest().mutate()
//                    .header(HttpHeaders.COOKIE, cookieHeader)
//                    .build();
//        }
//
//
//
//
//
//
//
//        // eredeti response-t becsomagoljuk egy dekorátorba
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
//
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                List<String> cookies = getHeaders().get("Set-Cookie");
//
//                if (cookies != null && !cookies.isEmpty()) {
//
//                    List<String> filtered = new ArrayList<>();
//                    for (String c : cookies) {
//                        System.out.println("CCCCC:"+c);
//
//                        if (c.startsWith("SESSION=")) {
//                            System.out.println("[SessionFilter] Found SESSION cookie in response: " + c);
//
//                            String targetDomain = null;
//                            targetDomain = "game.dominions.hu";
//                            String value = c.split(";", 2)[0].substring("SESSION=".length());
//
//                            // új cookie építése
//                            ResponseCookie newCookie = ResponseCookie.from("SESSION", value)
//                                    .path("/")
//                                    .httpOnly(true)
//                                    .secure(true)
//                                    .sameSite("None")
//                                    .maxAge(Duration.ofHours(3))
//                                    .domain(targetDomain)
//                                    .build();
//
//                            System.out.println("[SessionFilter] 🔄 Replacing with: " + newCookie.toString());
//                            filtered.add(newCookie.toString());
//                        } else {
//                            // minden más cookie változatlanul megy
//                            filtered.add(c);
//                        }
//                    }
//
//                    // lecseréljük az eredeti Set-Cookie headert
//                    getHeaders().remove("Set-Cookie");
//                    getHeaders().put("Set-Cookie", filtered);
//                } {
//                    System.out.println("[SessionFilter]: No cookies found in response!" );
//                }
//
//                // és továbbengedjük a választ
//                return super.writeWith(body);
//            }
//        };
//
//        // response lecserélése a módosítottra
////        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//
//
//
//
//        // 3) chain.filter a módosított request + dekorált response kombinációval
//        ServerWebExchange mutatedExchange = exchange.mutate()
//                .request(mutatedRequest)
//                .response(decoratedResponse)
//                .build();

        return chain.filter(exchange);
    }



//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//
//            // Az Apache Gateway már megkapta a választ a backendtől
//            List<String> cookies = exchange.getResponse().getHeaders().get("Set-Cookie");
//
//            if (cookies == null || cookies.isEmpty()) {
//                System.out.println("[SessionFilter] No Set-Cookie header in response.");
//                return;
//            }
//
//            List<String> newCookies = new ArrayList<>();
//
//            // lekérjük az Origin-t, hogy honnan jött a kérés
//            String origin = exchange.getRequest().getHeaders().getOrigin();
//            String targetDomain = null;
//
//            if ("https://game.dominions.hu".equals(origin)) {
//                targetDomain = "game.dominions.hu";
//            } else if ("https://admin.dominions.hu".equals(origin)) {
//                targetDomain = "admin.dominions.hu";
//            }
//
//            for (String cookie : cookies) {
//                if (cookie.startsWith("SESSION=")) {
//                    System.out.println("[SessionFilter] Found SESSION cookie: " + cookie);
//
//                    // értéket kivesszük
//                    String SessionKey = cookie.split(";", 2)[0].substring("SESSION=".length());
//
//                    // új cookie építése
//                    ResponseCookie newCookie = ResponseCookie.from("SESSION", SessionKey)
//                            .path("/")
//                            .httpOnly(true)
//                            .secure(true)
//                            .sameSite("None")
//                            .domain("game.dominions.hu")
//                            .maxAge(Duration.ofHours(3))
//                            .build();
//
//                    System.out.println("[SessionFilter] 🔄 Replacing with: " + newCookie.toString());
//                    newCookies.add(newCookie.toString());
//                } else {
//                    // minden más cookie változatlanul megy tovább
//                    newCookies.add(cookie);
//                }
//            }
//
//            // felülírjuk a headert
//            exchange.getResponse().getHeaders().remove("Set-Cookie");
//            exchange.getResponse().getHeaders().put("Set-Cookie", newCookies);
//
//        }));
//    }


    // EZ AZ ALAP
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        System.out.println("-----------------[SessionFilter]----------------------");
//
//        // Determine session id
//        String sessionId = exchange.getRequest()
//                .getCookies()
//                .getFirst("SESSION") != null
//                ? exchange.getRequest().getCookies().getFirst("SESSION").getValue()
//                : null;
//
//
//        if (sessionId != null) {
//            System.out.println("[SessionFilter]: request.session-id:" + sessionId);
//        } else {
//            System.out.println("[SessionFilter]: No request.session-id found!");
//        }
//
//        if(sessionId==null){
//            sessionId="new-session";
//        }
//
//        // Configure session based on  request-origin (the domain in the browser)
//        ResponseCookie.ResponseCookieBuilder cookieBuilder = null;
//
//        String requestOrigin = determineRequestOrigin(exchange);
//        System.out.println("[SessionFilter]: requestOrigin:" + requestOrigin);
//
//
//        if (requestOrigin == null) {
//            System.out.println("[SessionFilter]: Chosen session-mode: NONE");
//            System.out.println("[SessionFilter]: Chosen session-mode: null");
//        } else if (requestOrigin.startsWith("https://game.dominions.hu")) {
//            System.out.println("[SessionFilter]: Chosen session-mode: GAME");
//
//            cookieBuilder = ResponseCookie.from("SESSION", sessionId)
//                    .path("/")
//                    .httpOnly(true)
//                    .secure(true)
//                    .sameSite("None") // CORS miatt kötelező!
//                    .domain("game.dominions.hu");
//
//        } else if (requestOrigin.startsWith("https://admin.dominions.hu")) {
//            System.out.println("[SessionFilter]: Chosen session-mode: ADMIN");
//
//            cookieBuilder = ResponseCookie.from("SESSION", sessionId)
//                    .path("/")
//                    .httpOnly(true)
//                    .secure(true)
//                    .sameSite("None") // CORS miatt kötelező!
//                    .domain("admin.dominions.hu");
//
//        }
//
//
//        // Build cookie
//        if (cookieBuilder != null) {
//            exchange.getResponse().addCookie(cookieBuilder.build());
//        }
//
//
//        return chain.filter(exchange);
//    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                List<String> cookies = getHeaders().get("Set-Cookie");
//
//                if (cookies != null && !cookies.isEmpty()) {
//                    List<String> filtered = new ArrayList<>();
//
//                    for (String c : cookies) {
//                        System.out.println("CCCC:"+c);
//                        if (c.startsWith("SESSION=")) {
//                            // Ha nincs benne Domain, akkor pótoljuk
//                            if (!c.contains("Domain=")) {
//                                String origin = exchange.getRequest().getHeaders().getFirst("Origin");
//                                String targetDomain = null;
//

    /// /                                if (origin != null) {
    /// /                                    if (origin.contains("game.dominions.hu")) targetDomain = "game.dominions.hu";
    /// /                                    if (origin.contains("admin.dominions.hu")) targetDomain = "admin.dominions.hu";
    /// /                                }
//                                targetDomain = "game.dominions.hu";
//                                if (targetDomain != null) {
//                                    String fixed = c + "; Domain=" + targetDomain + "; Secure";
//                                    System.out.println("[SessionFilter] 🛠 Rewriting SESSION cookie for: " + targetDomain);
//                                    filtered.add(fixed);
//                                } else {
//                                    System.out.println("[SessionFilter] ⚠ Keeping host-only SESSION (no Origin found): " + c);
//                                    filtered.add(c);
//                                }
//
//                            } else {
//                                // Már domain-es, megtartjuk
//                                filtered.add(c);
//                            }
//                        } else {
//                            filtered.add(c);
//                        }
//                    }
//
//                    getHeaders().remove("Set-Cookie");
//                    getHeaders().put("Set-Cookie", filtered);
//                }
//
//                return super.writeWith(body);
//            }
//        };
//
//        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//    }


//        @Override
//        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//
//
//            System.out.println("++++++++++++++++++++++++++ WEBFILTER +++++++++++++++++++++++++++++++++++++");
//            System.out.println("Request Method: " + exchange.getRequest().getMethod());
//            System.out.println("Request Path: " + exchange.getRequest().getURI().getPath());
//            System.out.println("Request Headers: " + exchange.getRequest().getHeaders());
//
//
//            // Megpróbáljuk lekérni a session cookie-t (JSESSIONID)
//            String sessionId = exchange.getRequest().getCookies().getFirst("SESSION") != null
//                    ? exchange.getRequest().getCookies().getFirst("SESSION").getValue()
//                    : null;
//            System.out.println("SESSION-FILTER: " + sessionId);
//
//            // Ha van JSESSIONID, akkor beállítjuk a válaszban is, hogy a mikroszolgáltatásokhoz eljusson
//            if (sessionId != null) {
//                exchange.getResponse().getHeaders().add("Set-Cookie", "SESSION=" + sessionId);
//            }
//
//            return chain.filter(exchange);  // Továbbküldjük a kérést
//        }


//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        System.out.println("-----------------[SessionFilter]----------------------");
//
//        // Determine session id
//        String sessionId = exchange.getRequest()
//                .getCookies()
//                .getFirst("SESSION") != null
//                ? exchange.getRequest().getCookies().getFirst("SESSION").getValue()
//                : null;
//
//
//        if (sessionId != null) {
//            System.out.println("[SessionFilter]: request.session-id:" + sessionId);
//        } else {
//            System.out.println("[SessionFilter]: No request.session-id found!");
//        }
//
//
//        // Configure session based on  request-origin (the domain in the browser)
//        ResponseCookie.ResponseCookieBuilder cookieBuilder = null;
//        if (sessionId != null) {
//            String requestOrigin = determineRequestOrigin(exchange);
//            System.out.println("[SessionFilter]: requestOrigin:" + requestOrigin);
//
//
//            if (requestOrigin == null) {
//                System.out.println("[SessionFilter]: Chosen session-mode: NONE");
//                System.out.println("[SessionFilter]: Chosen session-mode: null");
//            } else if (requestOrigin.startsWith("https://game.dominions.hu")) {
//                System.out.println("[SessionFilter]: Chosen session-mode: GAME");
//
//                cookieBuilder = ResponseCookie.from("SESSION", sessionId)
//                        .path("/")
//                        .httpOnly(true)
//                        .secure(true)
//                        .sameSite("Lax")
//                        .domain("game.dominions.hu");
//
//            } else if (requestOrigin.startsWith("https://admin.dominions.hu")) {
//                System.out.println("[SessionFilter]: Chosen session-mode: ADMIN");
//
//                cookieBuilder = ResponseCookie.from("SESSION", sessionId)
//                        .path("/")
//                        .httpOnly(true)
//                        .secure(true)
//                        .sameSite("Lax")
//                        .domain("admin.dominions.hu");
//
//            }
//        }
//
//        // Build cookie
//        if (cookieBuilder != null) {
//            exchange.getResponse().addCookie(cookieBuilder.build());
//        }
//
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                List<String> cookies = getHeaders().get("Set-Cookie");
//
//                if (cookies != null && !cookies.isEmpty()) {
//                    List<String> filtered = new ArrayList<>();
//
//                    for (String c : cookies) {
//                        System.out.println();
//
//                        if (c.startsWith("SESSION=")) {
//                            // Ha nincs benne Domain, akkor pótoljuk
//                            if (!c.contains("Domain=")) {
//                                String targetDomain = null;
//
//
//                                targetDomain = "game.dominions.hu";
//
//                                if (targetDomain != null) {
//                                    String fixed = c + "; Domain=" + targetDomain + "; Secure";
//                                    System.out.println("[SessionFilter] 🛠 Rewriting SESSION cookie for: " + targetDomain);
//                                    filtered.add(fixed);
//                                } else {
//                                    System.out.println("[SessionFilter] ⚠ Keeping host-only SESSION (no Origin found): " + c);
//                                    filtered.add(c);
//                                }
//
//                            } else {
//                                // Már domain-es, megtartjuk
//                                filtered.add(c);
//                            }
//                        } else {
//                            filtered.add(c);
//                        }
//                    }
//
//                    getHeaders().remove("Set-Cookie");
//                    getHeaders().put("Set-Cookie", filtered);
//                }
//
//                return super.writeWith(body);
//            }
//        };
//
//        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//    }
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        System.out.println("-----------------[SessionFilter]----------------------");
//
//        // Determine session id
//        String sessionId = exchange.getRequest()
//                .getCookies()
//                .getFirst("SESSION") != null
//                ? exchange.getRequest().getCookies().getFirst("SESSION").getValue()
//                : null;
//
//
//        if (sessionId != null){
//            System.out.println("[SessionFilter]: request.session-id:"+sessionId);
//        }
//        else {
//            System.out.println("[SessionFilter]: No request.session-id found!");
//        }
//
//
//        List<String> cookies = exchange.getResponse().getHeaders().get("Set-Cookie");
//
//        if (cookies != null) {
//            List<String> newCookies = new ArrayList<>();
//            for (String cookieHeader : cookies) {
//                System.out.println("RESPONSE:" + cookieHeader);
//            }
//        }
//        else {
//            System.out.println("[SessionFilter]: No response cookie !");
//        }
//
//
//
//
//
//
//        // Configure session based on  request-origin (the domain in the browser)
//        ResponseCookie.ResponseCookieBuilder cookieBuilder = null;
//        if (sessionId != null) {
//            String requestOrigin = determineRequestOrigin(exchange);
//            System.out.println("[SessionFilter]: requestOrigin:" + requestOrigin);
//
//
//
//            if (requestOrigin == null) {
//                System.out.println("[SessionFilter]: Chosen session-mode: NONE");
//                System.out.println("[SessionFilter]: Chosen session-mode: null");
//            } else if (requestOrigin.startsWith("https://game.dominions.hu")) {
//                System.out.println("[SessionFilter]: Chosen session-mode: GAME");
//
//                cookieBuilder = ResponseCookie.from("SESSION", sessionId)
//                        .path("/")
//                        .httpOnly(true)
//                        .secure(true)
//                        .sameSite("Lax")
//                        .domain("game.dominions.hu");
//
//            } else if (requestOrigin.startsWith("https://admin.dominions.hu")) {
//                System.out.println("[SessionFilter]: Chosen session-mode: ADMIN");
//
//                cookieBuilder = ResponseCookie.from("SESSION", sessionId)
//                        .path("/")
//                        .httpOnly(true)
//                        .secure(true)
//                        .sameSite("Lax")
//                        .domain("admin.dominions.hu");
//
//            }
//        }
//
//        // Build cookie
//        if (cookieBuilder != null) {
//            exchange.getResponse().addCookie(cookieBuilder.build());
//        }
//
//        return chain.filter(exchange);
//    }
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


//    private boolean invocationFromSiteHosts(String originalHost) {
//
//        if (originalHost.startsWith("http://localhost:8080")) {
//            return true;
//        } else if (originalHost.startsWith("https://localhost:8080")) {
//            return true;
//        } else if (originalHost.startsWith("http://dominions.hu")) {
//            return true;
//        } else if (originalHost.startsWith("https://dominions.hu")) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean invocationFromAdminHosts(String originalHost) {
//        if (originalHost.startsWith("http://localhost:8081")) {
//            return true;
//        } else if (originalHost.startsWith("https://localhost:8081")) {
//            return true;
//        } else if (originalHost.startsWith("http://admin.dominions.hu")) {
//            return true;
//        } else if (originalHost.startsWith("https://admin.dominions.hu")) {
//            return true;
//        }
//        return false;
//    }
//
//
//    private String determineRequestOrigin(ServerWebExchange exchange) {
//        String origin = exchange.getRequest().getHeaders().getFirst("Origin");
//        String referer = exchange.getRequest().getHeaders().getFirst("Referer");
//
//        if (referer != null && !referer.equals("")) {
//            return referer;
//
//        } else if (origin != null && !origin.equals("")) {
//            return origin;
//
//        }
//
//        return null;
//    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//        System.out.println("++++++++++++++++++++++++++ SESSION FILTER +++++++++++++++++++++++++++++++++++++");
//
//        // Determination of request origin (the domain in the browser)
//        String requestOrigin = determineRequestOrigin(exchange);
//        if (requestOrigin == null) {
//            throw new RuntimeException("RequestOrigin is null!");
//        }
//        // Debugging
//        System.out.println("[SESSION-FILTER] Request Method: " + exchange.getRequest().getMethod());
//        System.out.println("[SESSION-FILTER] Request Path: " + exchange.getRequest().getURI().getPath());
//        System.out.println("[SESSION-FILTER] Request Headers: " + exchange.getRequest().getHeaders());
//        System.out.println("[SESSION-FILTER] Original RequestOrigin: " + requestOrigin);
//
//        String sessionId = null;
//        boolean isAdmin = false;
//        boolean isSite = false;
//        if (invocationFromAdminHosts(requestOrigin)) {
//            isAdmin = true;
//            System.out.println("[SESSION-FILTER] Invocation side: ADMIN");
//            if (exchange.getRequest().getCookies().getFirst("ADMINSESSION") != null) {
//                sessionId = exchange.getRequest().getCookies().getFirst("ADMINSESSION").getValue();
//
//                System.out.println("[SESSION-FILTER] sessionType: ADMINSESSION");
//                System.out.println("[SESSION-FILTER] sessionId: " + sessionId);
//                if (sessionId != null) {
//                    RequestCookie cookie =RequestCookie.from("SESSION", sessionId)
//                            .path("/")  // It will be a global cookie under the domain
//                            .httpOnly(true) // Javascript unable to access it
//                            .secure(false)  // false=> http allowed
//                            .build();
//                    exchange.getResponse().addCookie(cookie);
//
//                    exchange.getResponse().getHeaders().add("X-Session-Type", "ADMIN");
//                    exchange.getResponse().getHeaders().add("X-Session-Id", sessionId);
//                }
//            }
//        } else if (invocationFromSiteHosts(requestOrigin)) {
//            isSite = true;
//            System.out.println("[SESSION-FILTER] Invocation side: SITE");
//            if (exchange.getRequest().getCookies().getFirst("SITESESSION") != null) {
//                sessionId = exchange.getRequest().getCookies().getFirst("SITESESSION").getValue();
//
//                System.out.println("[SESSION-FILTER] sessionType: SITESESSION");
//                System.out.println("[SESSION-FILTER] sessionId: " + sessionId);
//                if (sessionId != null) {
//                    ResponseCookie cookie = ResponseCookie.from("SESSION", sessionId)
//                            .path("/")  // It will be a global cookie under the domain
//                            .httpOnly(true) // Javascript unable to access it
//                            .secure(false)  // false=> http allowed
//                            .build();
//                    exchange.getResponse().addCookie(cookie);
//
//                    exchange.getResponse().getHeaders().add("X-Session-Type", "SITE");
//                    exchange.getResponse().getHeaders().add("X-Session-Id", sessionId);
//                }
//            }
//        } else {
//            System.out.println("[SESSION-FILTER] Unable to determine the request origin! Request origin: " + requestOrigin);
//            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//            return exchange.getResponse().setComplete();
//        }
//
//        //return chain.filter(exchange);  // Request is forwarded after optional alterations
//
//
//        // --- RESPONSE irány: SESSION -> ADMINSESSION / SITESESSION ---
//        final String finalSessionId = sessionId;
//        final boolean finalIsAdmin = isAdmin;
//        final boolean finalIsSite = isSite;
//        final String finalRequestOrigin = requestOrigin;
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            //
//            String publicSessionName = null;
//            if (finalIsAdmin) {
//                publicSessionName = "ADMINSESSION";
//            } else if (finalIsSite) {
//                publicSessionName = "SITESESSION";
//            }
//
//            //
//            String privateSessionId = null;
//            if (exchange.getResponse().getCookies().getFirst("SESSION") != null) {
//                privateSessionId = exchange.getResponse().getCookies().getFirst("SESSION").getValue();
//            }
//
//            //
//            if (privateSessionId != null) {
//                if (publicSessionName != null) {
//                    ResponseCookie responseCookie = ResponseCookie.from(publicSessionName, privateSessionId)
//                            .path("/")
//                            .httpOnly(true)
//                            .secure(false)
//                            .build();
//                    exchange.getResponse().addCookie(responseCookie);
//                    exchange.getResponse().getCookies().remove("SESSION");
//                } else {
//                    ResponseCookie responseCookie= ResponseCookie.from("SESSSION_X", privateSessionId)
//                            .path("/")
//                            .httpOnly(true)
//                            .secure(false)
//                            .build();
//                    exchange.getResponse().addCookie(responseCookie);
//                    exchange.getResponse().getCookies().remove("SESSION");
//                }
//            }
//
//            System.out.println("[SESSION-FILTER.Response] header: " + exchange.getRequest().getHeaders());
//            System.out.println("[SESSION-FILTER.Response] publicSessionName: " + publicSessionName);
//        }));
//    }


//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        System.out.println("[FilterChain]: SessionFilter");
//        // Determination of request origin (the domain in the browser)
//        String requestOrigin = determineRequestOrigin(exchange);
//        if (requestOrigin == null) {
//            throw new RuntimeException("RequestOrigin is null!");
//        }
//
//        //
//        String requestSessionId = null;
//        boolean isAdmin = invocationFromAdminHosts(requestOrigin);
//        boolean isSite = invocationFromSiteHosts(requestOrigin);
//        HttpCookie adminSessionCookieObj = exchange.getRequest().getCookies().getFirst("ADMINSESSION");
//        HttpCookie siteSessionCookieObj = exchange.getRequest().getCookies().getFirst("SITESESSION");
//
//
//        ServerWebExchange manipulatedExchange = null;
//
//        //
//        if (isAdmin) {
//            if (adminSessionCookieObj != null) {
//                requestSessionId = adminSessionCookieObj.getValue();
//                if (requestSessionId != null) {
//
//                    manipulatedExchange = exchange.mutate().request(
//                                    exchange.getRequest().mutate()
//                                            .header("Cookie", "SESSION=" + requestSessionId)
//                                            .header("X-Session-Type", "ADMIN")
//                                            .header("X-Session-Id", requestSessionId)
//                                            .build()
//                            )
//                            .build();
//                }
//            }
//        } else if (isSite) {
//            if (siteSessionCookieObj != null) {
//                requestSessionId = siteSessionCookieObj.getValue();
//
//                if (requestSessionId != null) {
//                    manipulatedExchange = exchange.mutate().request(
//                                    exchange.getRequest().mutate()
//                                            .header("Cookie", "SESSION=" + requestSessionId)
//                                            .header("X-Session-Type", "SITE")
//                                            .header("X-Session-Id", requestSessionId)
//                                            .build()
//                            )
//                            .build();
//                }
//            }
//        } else {
//            System.out.println("[SESSION-FILTER] Unable to determine the request origin! Request origin: " + requestOrigin);
//            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//            return exchange.getResponse().setComplete();
//        }
//
//
//        if (manipulatedExchange == null) {
//            manipulatedExchange = exchange;
//        }
//
//        final ServerWebExchange finalExchange = manipulatedExchange;
//        return chain.filter(finalExchange);
//        return chain.filter(finalExchange)
//                .doOnSuccess(done -> {
//                    finalExchange.getResponse().beforeCommit(() -> {
//                        ResponseCookie expiredSession = ResponseCookie.from("SESSION", "")
//                                .path("/")
//                                .maxAge(0) // azonnal lejár
//                                .build();
//                        finalExchange.getResponse().addCookie(expiredSession);
//
//                        // Majd beállítjuk az ADMINSESSION-t
//                        ResponseCookie adminCookie = ResponseCookie.from("ADMINSESSION", "77777")
//                                .path("/")
//                                .httpOnly(true)
//                                .secure(false)
//                                .build();
//                        finalExchange.getResponse().addCookie(adminCookie);
//
//                        System.out.println("[SESSION-FILTER][RESPONSE] ADMINSESSION cookie hozzáadva (beforeCommit)");
//
//                        return Mono.empty();
//                    });
//                });
//    }
//}
