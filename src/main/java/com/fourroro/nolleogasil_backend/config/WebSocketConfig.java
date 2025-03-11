package com.fourroro.nolleogasil_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker // 웹소켓 메시지 핸들링 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.username}")
    private String rabbitUser;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPw;

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;
    // STOMP에서 사용하는 메시지 브로커 설정
        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            // 파라미터로 지정한 prefix가 붙은 메시지를 발행할 경우, 메시지 브로커가 이를 처리하게 된다.
            // 메시지를 구독하는 요청 url => 즉 메시지 받을 때
            //config.enableSimpleBroker("/chat"); -- STOMP (인메모리 브로커사용시) //해당 주소를 구독하고 있는 클라이언트들에게 메세지 전달
            config.enableStompBrokerRelay( "/exchange","/topic")
                    .setRelayHost(rabbitHost) //rabbitmq 설정
                    .setRelayPort(rabbitPort)
                    .setVirtualHost("/")
                    .setClientLogin(rabbitUser)
                    .setClientPasscode(rabbitPw);
            config.setPathMatcher(new AntPathMatcher("."));
            // RabbitMq 에서는 기본적으로 queue,exchange 의 이름이나 라우팅 키, 패턴등을 작성할 때 . 을 구분자로 사용
            // 요청 경로로 사용될때 url 에서 계층 구분자로 사용되는 / 와 혼동이 생길 수 있기때문에

            // 메세지 핸들러로 라우팅 되는 prefix를 파라미터로 지정할 수 있다.
            // 메시지 가공 처리가 필요한 경우, 가공 핸들러로 메시지를 라우팅 되도록하는 설정
            // 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
            // @Controller 객체의 @MessageMapping 메서드로 라우팅
            config.setApplicationDestinationPrefixes(("/pub")); //클라이언트에서 보낸 메세지를 받을 prefix
        }


        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // websocket 또는 sockJs client 가 웹소켓 핸드셰이크 커넥션을 생성할 경로
            registry.addEndpoint(("/ws")) //SockJS 연결 주소.
                    .setAllowedOriginPatterns("*")
                    .addInterceptors(webSocketHandshakeInterceptor)
                    .withSockJS();


        }


}


