package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.saemoim.domain.Report;
import com.saemoim.domain.User;
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.repository.ReportRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
	@InjectMocks
	private ReportServiceImpl reportService;
	@Mock
	private ReportRepository reportRepository;
	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("신고받은 사용자 조회")
	void getReportedUsers() {
		List<Report> list = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 10);

		when(reportRepository.findAllByOrderByCreatedAt(pageable)).thenReturn(new PageImpl<>(list, pageable, 0));
		//when
		Page<ReportResponseDto> page = reportService.getReportedUsers(pageable);
		//then
		verify(reportRepository).findAllByOrderByCreatedAt(pageable);
		assertThat(page.getContent()).isEqualTo(list.stream().map(ReportResponseDto::new).collect(Collectors.toList()));
	}

	@Test
	@DisplayName("사용자 신고")
	void reportUser() {
		//given
		ReportRequestDto requestDto = new ReportRequestDto("content");
		String reporterName = "jun";
		User subjectUser = mock(User.class);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(subjectUser));
		when(reportRepository.existsByReporterNameAndSubject(reporterName, subjectUser)).thenReturn(false);

		//when
		reportService.reportUser(anyLong(), requestDto, reporterName);
		//then
		verify(reportRepository).save(any(Report.class));
	}

	@Test
	@DisplayName("신고 삭제")
	void deleteReport() {
		//given
		Report report = mock(Report.class);
		when(reportRepository.findById(anyLong())).thenReturn(Optional.of(report));
		//when
		reportService.deleteReport(anyLong());
		//then
		verify(reportRepository).delete(report);
	}
}