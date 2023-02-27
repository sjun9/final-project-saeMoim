package com.saemoim.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Report;
import com.saemoim.domain.User;

public interface ReportRepository extends JpaRepository<Report, Long> {
	Page<Report> findAllByOrderByCreatedAt(Pageable pageable);

	void deleteAllBySubject_Id(Long subjectId);

	boolean existsByReporterNameAndSubject(String reporterName, User subject);
}
