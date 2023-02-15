package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllByGroup_IdOrderByCreatedAtDesc(Long groupId);

}
