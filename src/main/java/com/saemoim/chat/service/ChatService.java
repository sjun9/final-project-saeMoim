package com.saemoim.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.chat.domain.Chat;
import com.saemoim.chat.dto.ChatRequestDto;
import com.saemoim.chat.dto.ChatResponseDto;
import com.saemoim.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;

	// 메세지 가져오기 - 최대 100개제한 있으면 좋을듯
	// DB 메세지 - 한달 단위로 삭제 - 자동 크론?
	@Transactional
	public List<ChatResponseDto> getAllChatByGroupId(Long groupId) {
		return chatRepository.findAllByGroupIdOrderByCreatedAtAsc(groupId).stream()
			.map(ChatResponseDto::new).collect(Collectors.toList());
	}

	// 메세지 저장
	@Transactional
	public ChatResponseDto saveMessage(ChatRequestDto chatMessage) {
		return new ChatResponseDto(chatRepository.save(new Chat(chatMessage)));
	}
}
