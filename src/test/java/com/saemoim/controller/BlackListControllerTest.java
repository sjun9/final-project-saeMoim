package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.BlackListServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = BlackListController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BlackListControllerTest {

	@MockBean
	private BlackListServiceImpl blackListService;

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@MockBean
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	@DisplayName("블랙리스트 조회")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void getBlacklists() throws Exception {
		//given
		List<BlackListResponseDto> blacklist = new ArrayList<>();
		BlackListResponseDto responseDto = BlackListResponseDto.builder()
			.id(1L)
			.userId(1L)
			.username("reporter")
			.banCount(1)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now()).build();
		blacklist.add(responseDto);
		when(blackListService.getBlacklists()).thenReturn(blacklist);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/blacklist")
			.header("Authorization", "Bearer adminAccessToken"));
		//then
		verify(blackListService).getBlacklists();
		resultActions.andExpect(status().isOk())
			.andDo(document("blacklist/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("어드민계정 엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("블랙리스트"),
					fieldWithPath("data.[].id").description("블랙리스트 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].userId").description("피신고인 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].username").description("신고인 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].banCount").description("신고 당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].createdAt").description("생성일자").type(JsonFieldType.STRING),
					fieldWithPath("data.[].modifiedAt").description("수정일자").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("블랙리스트 등록")
	void addBlacklist() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("블랙리스트 등록이 완료 되었습니다.");
		doNothing().when(blackListService).addBlacklist(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/blacklist/users/{userId}", 1L));
		//then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("data").value(responseDto.getData()));
	}

	@Test
	@DisplayName("영구 블랙리스트 등록")
	void imposePermanentBan() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("영구 블랙리스트 등록이 완료 되었습니다.");
		doNothing().when(blackListService).imposePermanentBan(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.patch("/admin/blacklists/{blacklistId}", 1L));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists());
	}

	@Test
	@DisplayName("블랙리스트 해제")
	void deleteBlacklist() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("블랙리스트 해제 완료");
		doNothing().when(blackListService).deleteBlacklist(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/admin/blacklists/{blacklistId}", 1L));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists());
	}
}