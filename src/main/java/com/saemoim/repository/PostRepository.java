package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByGroup_Id(Long group_id);

}
