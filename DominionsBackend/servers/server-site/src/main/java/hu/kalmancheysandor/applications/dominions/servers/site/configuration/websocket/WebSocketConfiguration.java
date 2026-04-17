package hu.kalmancheysandor.applications.dominions.servers.site.configuration.websocket;


import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                Principal user = SimpMessageHeaderAccessor.getUser(message.getHeaders());

                System.out.println("THE PRINCIPAL IS: " + user.getName());
                // debug here
                return message;
            }
        });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry
                .addEndpoint("/spring-boot-chat")
                .setAllowedOrigins(
                        "http://localhost:8080",
                        "http://dominions.hu",
                        "https://game.dominions.hu"
                )

//            .setAllowedOrigins("*")
                .setHandshakeHandler(
                        new DefaultHandshakeHandler() {

                            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map attributes) throws Exception {
                                System.out.println("HandshakeHandler---------------------------------------------------");
                                if (request instanceof ServletServerHttpRequest) {
                                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                                    HttpSession session = servletRequest.getServletRequest().getSession();
                                    attributes.put("sessionId", session.getId());
                                    System.out.println("----sessionId:" + session.getId());
                                } else {
                                    System.out.println("Not ServletServerHttpRequest");
                                }
                                return true;
                            }
                        })

                .withSockJS();
    }

//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}