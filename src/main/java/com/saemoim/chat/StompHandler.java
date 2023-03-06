package com.saemoim.chat;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.saemoim.exception.ErrorCode;
import com.saemoim.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
	private final JwtUtil jwtUtil;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); // 헤더에 있는 토큰값을 가져오기 위해 StompHeaderAccessor.wrap()
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			if (!jwtUtil.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7))) {
				throw new IllegalArgumentException(ErrorCode.INVALID_AUTH_TOKEN.getMessage());
			}
		}
		return message;
	}
}