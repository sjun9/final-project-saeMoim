package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.BlackList;
import com.saemoim.domain.User;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

	boolean existsByUser(User user);

	List<BlackList> findAllByOrderByCreatedAtDesc();

	@Query("delete from BlackList b where b in (:blacklists)")
	@Modifying
	void deleteBlackLists(@Param("blacklists") List<BlackList> blackLists);
}
