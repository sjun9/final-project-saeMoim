package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.ReviewLike;

public interface LikeRepository extends JpaRepository<ReviewLike ,Long> {

	int countBy();
	boolean existsByReview_IdAndUser_Id(Long reviewId, Long userId);
	void deleteByReview_IdAndUser_Id(Long reviewId, Long userId);
}
