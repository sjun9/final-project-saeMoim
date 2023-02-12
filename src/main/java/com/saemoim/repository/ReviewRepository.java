package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllByGroupIdOrderByCreatedAt(Long groupId);
	
	@Query(value = "select r from Review r join fetch r.group where r.group.id in (:groupIdList)")
	List<Review> findAllReviewsByGroupId(@Param("groupIdList") List<Long> groupIdList);
}
