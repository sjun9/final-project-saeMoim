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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
		//given
		List<Report> list = new ArrayList<>();

		when(reportRepository.findAllByOrderByCreatedAt()).thenReturn(list);
		//when
		List<ReportResponseDto> list1 = reportService.getReportedUsers();
		//then
		verify(reportRepository).findAllByOrderByCreatedAt();
		assertThat(list1).isEqualTo(list.stream().map(ReportResponseDto::new).toList());
	}

	@Test
	@DisplayName("사용자 신고")
	void reportUser() {
		//given
		ReportRequestDto requestDto = new ReportRequestDto("content");
		String reporterName = "jun";
		User subjectUser = mock(User.class);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(subjectUser));
		when(reportRepository.existsByReporterAndSubject(reporterName, subjectUser)).thenReturn(false);

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