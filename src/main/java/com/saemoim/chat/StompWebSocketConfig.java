package com.saemoim.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/stomp/chat") // 여기로 웹소켓 생성

			.setAllowedOriginPatterns( // for intelliJ
				"http://localhost:8080",
				"http://127.0.0.1:8080",
				"http://43.201.17.85:8080",

				"http://localhost:63342",
				"http://127.0.0.1:63342",
				"http://43.201.17.85:63342",

				"https://api.saemoim.site"
			)
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/pub");
		registry.enableSimpleBroker("/sub");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// 들어오는 메세지들(Inbound)이 이 곳을 거치며 토큰값을 확인한다
		// 토큰 불일치 시 예외 발생으로 통신 불가
		registration.interceptors(stompHandler);
	}
}
