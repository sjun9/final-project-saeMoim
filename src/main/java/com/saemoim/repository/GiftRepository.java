package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Gift;

public interface GiftRepository extends JpaRepository<Gift, Long> {
	List<Gift> findByUser_IdOrderByCreatedAtDesc(Long userId);
	List<Gift> findByEvent_IdOrderByCreatedAt(Long eventId);
	boolean existsByEvent_IdAndUser_Id(Long eventId, Long userId);
}
