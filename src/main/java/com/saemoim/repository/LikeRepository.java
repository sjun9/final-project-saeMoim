package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.PostLike;

public interface LikeRepository extends JpaRepository<PostLike,Long> {

	boolean existsByPost_IdAndUser_Id(Long postId, Long userId);
	void deleteByPost_IdAndUser_Id(Long postId, Long userId);

	void deleteByPost_Id(Long postId);
}
