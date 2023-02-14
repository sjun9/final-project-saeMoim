package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.User;
import com.saemoim.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
	List<Wish> findAllByUserOrderByCreatedAtDesc(User user);
}
