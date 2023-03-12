package com.saemoim.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findAllByGroup_IdOrderByCreatedAtDesc(Long groupId, Pageable pageable);

	boolean existsByGroup_IdAndUser_Id(Long groupId, Long userId);
}
