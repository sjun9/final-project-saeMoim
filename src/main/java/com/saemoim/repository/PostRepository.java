package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
