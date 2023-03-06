package com.saemoim.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	List<Chat> findAllByGroupIdOrderByCreatedAtAsc(Long groupId);
}
