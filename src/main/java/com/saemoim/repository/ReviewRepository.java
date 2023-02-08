package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
