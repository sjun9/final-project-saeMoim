package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Application;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	List<Application> findAllByUserOrderByCreatedAtDesc(User user);

	@Query(value = "select distinct a from Application a join fetch a.group where a.group in :groups")
	List<Application> findAllByGroups(@Param("groups") List<Group> groups);

	boolean existsByUserAndGroup(User user, Group group);
}
