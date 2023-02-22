package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Report;
import com.saemoim.domain.User;

public interface ReportRepository extends JpaRepository<Report, Long> {
	List<Report> findAllByOrderByCreatedAt();

	void deleteAllBySubject_Id(Long subjectId);

	boolean existsByReporterNameAndSubject(String reporterName, User subject);
}
