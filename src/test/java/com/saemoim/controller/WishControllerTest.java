package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.WishService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = WishController.class)
@MockBean(JpaMetamodelMappingContext.class)
class WishControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private WishService wishService;
	@MockBean
	private AWSS3Uploader awsS3Uploader;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@MockBean
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	@MockBean
	private CustomOAuth2UserService oAuth2UserService;
	@MockBean
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	@WithCustomMockUser
	@DisplayName("관심모임 조회")
	void getWishGroups() throws Exception {
		// given
		var response = GroupResponseDto.builder()
			.id(1L)
			.tags(new ArrayList<>())
			.categoryName("category")
			.groupName("moim")
			.userId(1L)
			.username("leader")
			.content("모임소개글")
			.address("서울시 종로구 혜화동")
			.firstRegion("서울시")
			.secondRegion("종로구")
			.latitude("위도")
			.longitude("경도")
			.status(GroupStatusEnum.OPEN)
			.wishCount(2)
			.recruitNumber(3)
			.participantCount(1)
			.imagePath("image path")
			.build();
		List<GroupResponseDto> list = new ArrayList<>();
		list.add(response);
		when(wishService.getWishGroups(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/group/wish")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("wish/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("관심모임목록"),
					fieldWithPath("data.[].id").description("모임 id").type(JsonFieldType.NUMBER),
					subsectionWithPath("data.[].tags").description("모임태그목록"),
					fieldWithPath("data.[].categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.[].userId").description("모임 리더 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].username").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("data.[].address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("data.[].firstRegion").description("장소 세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("data.[].secondRegion").description("장소 세부; 시/군/구")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.[].latitude").description("장소 세부; 위도").type(JsonFieldType.STRING),
					fieldWithPath("data.[].longitude").description("장소 세부; 경도").type(JsonFieldType.STRING),
					fieldWithPath("data.[].status").description("장소 상태(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.[].wishCount").description("찜당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].recruitNumber").description("모집인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].participantCount").description("참여인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].imagePath").description("이미지 파일 저장 경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("관심모임에 추가")
	void wishGroup() throws Exception {
		// given
		var groupId = 1L;
		doNothing().when(wishService).addWishGroup(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/groups/{groupId}/wish", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("wish/wish",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("모임 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("관심모임 삭제")
	void deleteWishGroup() throws Exception {
		// given
		var groupId = 1L;
		doNothing().when(wishService).deleteWishGroup(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/groups/{groupId}/wish", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("wish/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("모임 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}
}