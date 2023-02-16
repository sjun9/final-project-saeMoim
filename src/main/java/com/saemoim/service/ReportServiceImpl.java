package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Report;
import com.saemoim.domain.User;
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.ReportRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	private ReportRepository reportRepository;
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ReportResponseDto> getReportedUsers() {
		return reportRepository.findAllByOrderByCreatedAt().stream().map(ReportResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void reportUser(Long subjectUserId, ReportRequestDto requestDto, String reporterName) {
		User subjectUser = userRepository.findById(subjectUserId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		if (reportRepository.existsByReporterNameAndSubject(reporterName, subjectUser)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_REPORT.getMessage());
		}

		reportRepository.save(new Report(subjectUser, reporterName, requestDto.getContent()));
	}

	@Transactional
	@Override
	public void deleteReport(Long reportId) {
		Report report = reportRepository.findById(reportId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_REPORT.getMessage())
		);

		reportRepository.delete(report);
	}
}
