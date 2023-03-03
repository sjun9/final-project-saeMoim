package com.saemoim.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// @Controller
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	ChatHandler chatHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(chatHandler, "ws/chat")
			// .setAllowedOrigins("*") // 나중에 직접 하나씩 지정 할 것
			// .setAllowedOriginPatterns("*") // 나중에 직접 하나씩 지정 할 것
			// .setAllowedOriginPatterns("http://*:8080", "http://*.*.*.*:8080")
			.setAllowedOriginPatterns("http://*:63342", "http://*.*.*.*:63342") // 인텔리제이로 페이지 오픈하기 때문
			.withSockJS()
			.setClientLibraryUrl("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.js");
	}
}