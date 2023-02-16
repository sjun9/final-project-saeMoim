package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.Report;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.service.ReportServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ReportController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReportControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	ReportServiceImpl reportService;

	@Test
	@DisplayName("신고된 사용자 조회")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void getReportedUsers() throws Exception {
		//given
		User user = User.builder().username("jun").content("aaaaa").build();
		Report report = new Report(user, "ssss", "ggggg");
		List<ReportResponseDto> list = new ArrayList<>();
		list.add(new ReportResponseDto(report));
		when(reportService.getReportedUsers()).thenReturn(list);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/report"));
		//then
		resultActions.andExpect(jsonPath("$[0]['reporterName']").value("ssss"));
		resultActions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자 신고")
	@WithCustomMockUser
	void reportUser() throws Exception {
		//given
		Long subjectUserId = 1L;
		ReportRequestDto requestDto = ReportRequestDto.builder().content("신고내용").build();
		//when
		ResultActions resultActions = mockMvc.perform(post("/report/users/{subjectUserId}", subjectUserId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto))
			.with(csrf()));
		//then
		resultActions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("신고내역 삭제")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void deleteReport() throws Exception {
		//given
		doNothing().when(reportService).deleteReport(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/admin/report/{reportId}", 1L).with(csrf()));
		//then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("message").value("신고 삭제 완료"));
	}
}