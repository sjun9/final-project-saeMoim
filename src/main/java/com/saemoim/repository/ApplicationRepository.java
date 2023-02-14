package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Application;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	List<Application> findAllByUserOrderByCreatedAtDesc(User user);

	List<Application> findAllByGroupOrderByCreatedAt(Group group);

	boolean existsByUserAndGroup(User user, Group group);
}
