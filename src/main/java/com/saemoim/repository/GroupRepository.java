package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;

public interface GroupRepository extends JpaRepository<Group, Long> {
	List<Group> findAllByOrderByCreatedAtDesc();

	List<Group> findAllByUserOrderByCreatedAtDesc(User user);

	List<Group> findAllByCategoryOrderByCreatedAtDesc(Category category);

}
