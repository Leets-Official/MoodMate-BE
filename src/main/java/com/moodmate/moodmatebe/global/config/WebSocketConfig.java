package com.moodmate.moodmatebe.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Slf4j
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chat")
                .setAllowedOrigins("localhost:8080")
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //pub 로 데이터를 받으면 이곳을 한번 거쳐서 URI 만 MessageMapping 에 매핑
        registry.setApplicationDestinationPrefixes("/pub");
        //해당 주소를 구독하고 있는 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/sub");
    }
}