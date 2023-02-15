package com.saemoim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
	List<Wish> findAllByUserOrderByCreatedAtDesc(User user);

	Optional<Wish> findByUserAndGroup(User user, Group group);

	boolean existsByUserAndGroup(User user, Group group);
}
