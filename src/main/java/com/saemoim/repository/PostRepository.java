package com.saemoim.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByGroup_Id(Long group_id, Pageable pageable);

}
