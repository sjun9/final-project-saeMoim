package com.saemoim.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	List<Group> findAllByOrderByCreatedAtDesc(Pageable pageable);

	List<Group> findAllByUserIdOrderByCreatedAtDesc(Long userId);

	@Query(value = "select g from sae_group g where g.id in (:groupIdList)")
	List<Group> findAllByGroupIdList(@Param("groupIdList") List<Long> groupIdList);

}
