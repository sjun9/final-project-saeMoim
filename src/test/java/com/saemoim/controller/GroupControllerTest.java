package com.saemoim.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.GroupService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = GroupController.class)
@MockBean(JpaMetamodelMappingContext.class)
class GroupControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private GroupService groupService;
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
	@DisplayName("전체 모임조회")
	void getAllGroups() throws Exception {
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
		Slice<GroupResponseDto> sliceDto = new SliceImpl<>(list);

		when(groupService.getAllGroups(any(Pageable.class))).thenReturn(sliceDto);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/group"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					subsectionWithPath("data").description("결과값"),
					subsectionWithPath("data.content").description("모임목록"),
					fieldWithPath("data.content.[].id").description("모임 id").type(JsonFieldType.NUMBER),
					subsectionWithPath("data.content.[].tags").description("모임태그목록"),
					fieldWithPath("data.content.[].categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].userId").description("모임 리더 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].username").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].firstRegion").description("장소 세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].secondRegion").description("장소 세부; 시/군/구")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].latitude").description("장소 세부; 위도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].longitude").description("장소 세부; 경도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].status").description("장소 상태(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].wishCount").description("찜당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].recruitNumber").description("모집인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].participantCount").description("참여인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].imagePath").description("이미지 파일 저장 경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("특정 모임 조회")
	void getGroup() throws Exception {
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
		var groupId = 1L;
		when(groupService.getGroup(anyLong())).thenReturn(response);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/groups/{groupId}", groupId));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/get",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("조회 할 모임 id")
				),
				responseFields(
					fieldWithPath("id").description("모임 id").type(JsonFieldType.NUMBER),
					subsectionWithPath("tags").description("모임태그목록"),
					fieldWithPath("categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("userId").description("모임 리더 id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("firstRegion").description("장소 세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("secondRegion").description("장소 세부; 시/군/구")
						.type(JsonFieldType.STRING),
					fieldWithPath("latitude").description("장소 세부; 위도").type(JsonFieldType.STRING),
					fieldWithPath("longitude").description("장소 세부; 경도").type(JsonFieldType.STRING),
					fieldWithPath("status").description("장소 상태(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("wishCount").description("찜당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("recruitNumber").description("모집인원").type(JsonFieldType.NUMBER),
					fieldWithPath("participantCount").description("참여인원").type(JsonFieldType.NUMBER),
					fieldWithPath("imagePath").description("이미지 파일 저장 경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("인기 모임 조회")
	void getGroupByPopularity() throws Exception {
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

		when(groupService.getGroupByPopularity()).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/group/popular"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/get-popular",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					subsectionWithPath("data").description("인기모임 목록"),
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
	@DisplayName("카테고리와 상태 필터로 모임 조회")
	void getGroupsByCategoryAndStatus() throws Exception {
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
		Slice<GroupResponseDto> sliceDto = new SliceImpl<>(list);
		var categoryId = 2L;
		when(groupService.getGroupsByCategoryAndStatus(anyLong(), anyString(), any(Pageable.class))).thenReturn(
			sliceDto);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/group/categories/{categoryId}", categoryId)
				.param("status", "OPEN")
				.accept(MediaType.APPLICATION_JSON));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/filter",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("categoryId").description("카테고리 id")
				),
				queryParameters(
					parameterWithName("status").description("모임상태")
				),
				responseFields(
					subsectionWithPath("data").description("결과값"),
					subsectionWithPath("data.content").description("모임목록"),
					fieldWithPath("data.content.[].id").description("모임 id").type(JsonFieldType.NUMBER),
					subsectionWithPath("data.content.[].tags").description("모임태그목록"),
					fieldWithPath("data.content.[].categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].userId").description("모임 리더 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].username").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].firstRegion").description("장소 세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].secondRegion").description("장소 세부; 시/군/구")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].latitude").description("장소 세부; 위도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].longitude").description("장소 세부; 경도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].status").description("장소 상태(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].wishCount").description("찜당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].recruitNumber").description("모집인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].participantCount").description("참여인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].imagePath").description("이미지 파일 저장 경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("태그로 모임 검색")
	void getGroupsByTag() throws Exception {
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
		Slice<GroupResponseDto> sliceDto = new SliceImpl<>(list);
		when(groupService.searchGroupsByTag(anyString(), any(Pageable.class))).thenReturn(
			sliceDto);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/group/tag")
				.param("tagName", "trip")
				.accept(MediaType.APPLICATION_JSON));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/tag",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("tagName").description("태그 검색어")
				),
				responseFields(
					subsectionWithPath("data").description("결과값"),
					subsectionWithPath("data.content").description("모임목록"),
					fieldWithPath("data.content.[].id").description("모임 id").type(JsonFieldType.NUMBER),
					subsectionWithPath("data.content.[].tags").description("모임태그목록"),
					fieldWithPath("data.content.[].categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].userId").description("모임 리더 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].username").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].firstRegion").description("장소 세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].secondRegion").description("장소 세부; 시/군/구")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].latitude").description("장소 세부; 위도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].longitude").description("장소 세부; 경도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].status").description("장소 상태(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].wishCount").description("찜당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].recruitNumber").description("모집인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].participantCount").description("참여인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].imagePath").description("이미지 파일 저장 경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("이름으로 모임 검색")
	void searchGroups() throws Exception {
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
		Slice<GroupResponseDto> sliceDto = new SliceImpl<>(list);
		when(groupService.searchGroups(anyString(), any(Pageable.class))).thenReturn(
			sliceDto);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/group/name")
				.param("groupName", "moim")
				.accept(MediaType.APPLICATION_JSON));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/name",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("groupName").description("모임명 검색어")
				),
				responseFields(
					subsectionWithPath("data").description("결과값"),
					subsectionWithPath("data.content").description("모임목록"),
					fieldWithPath("data.content.[].id").description("모임 id").type(JsonFieldType.NUMBER),
					subsectionWithPath("data.content.[].tags").description("모임태그목록"),
					fieldWithPath("data.content.[].categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].userId").description("모임 리더 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].username").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].firstRegion").description("장소 세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].secondRegion").description("장소 세부; 시/군/구")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].latitude").description("장소 세부; 위도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].longitude").description("장소 세부; 경도").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].status").description("장소 상태(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].wishCount").description("찜당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].recruitNumber").description("모집인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].participantCount").description("참여인원").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].imagePath").description("이미지 파일 저장 경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("내가 만든 모임 조회")
	void getMyGroupsByLeader() throws Exception {
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
		when(groupService.getMyGroupsByLeader(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/leader/group")
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/leader",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("모임목록"),
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
	@DisplayName("참여중인 모임 조회")
	void getMyGroupsByParticipant() throws Exception {
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
		when(groupService.getMyGroupsByParticipant(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/participant/group")
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/participant",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("모임목록"),
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
	@DisplayName("모임 생성")
	void createGroup() throws Exception {
		// given
		MockMultipartFile image = new MockMultipartFile("img", "image.png", "image/png",
			"<<png data>>".getBytes());
		MockMultipartFile request = new MockMultipartFile("requestDto", "",
			"application/json", ("{ \"categoryName\": \"category\","
			+ "\"tagNames\": [\"tag\", \"name\"],"
			+ "\"name\": \"group name\","
			+ "\"content\": \"group content group content\","
			+ "\"address\": \"group address\","
			+ "\"firstRegion\": \"group firstRegion\","
			+ "\"secondRegion\": \"group secondRegion\","
			+ "\"latitude\": \"group latitude\","
			+ "\"longitude\": \"group longitude\","
			+ "\"recruitNumber\": 4}").getBytes());

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.multipart("/group")
			.file(image).file(request)
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("group/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				requestPartFields("requestDto",
					fieldWithPath("categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("tagNames").description("태그목록").type(JsonFieldType.ARRAY),
					fieldWithPath("name").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("firstRegion").description("주소세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("secondRegion").description("주소세부; 시/군/구").type(JsonFieldType.STRING),
					fieldWithPath("latitude").description("위도").type(JsonFieldType.STRING),
					fieldWithPath("longitude").description("경도").type(JsonFieldType.STRING),
					fieldWithPath("recruitNumber").description("모집인원").type(JsonFieldType.NUMBER)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("모임 수정")
	void updateGroup() throws Exception {
		// given
		MockMultipartFile image = new MockMultipartFile("img", "image.png", "image/png",
			"<<png data>>".getBytes());
		MockMultipartFile request = new MockMultipartFile("requestDto", "",
			"application/json", ("{ \"categoryName\": \"category\","
			+ "\"tagNames\": [\"tag\", \"name\"],"
			+ "\"name\": \"group name\","
			+ "\"content\": \"group content group content\","
			+ "\"address\": \"group address\","
			+ "\"firstRegion\": \"group firstRegion\","
			+ "\"secondRegion\": \"group secondRegion\","
			+ "\"latitude\": \"group latitude\","
			+ "\"longitude\": \"group longitude\","
			+ "\"recruitNumber\": 4}").getBytes());

		var groupId = 1L;
		// when
		MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder)multipart(
			HttpMethod.PUT, "/groups/{groupId}", groupId)
			.requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/groups/{groupId}");
		ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder
			.file(image).file(request)
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("group/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				pathParameters(
					parameterWithName("groupId").description("수정할 모임 id")
				),
				requestPartFields("requestDto",
					fieldWithPath("categoryName").description("카테고리 이름").type(JsonFieldType.STRING),
					fieldWithPath("tagNames").description("태그목록").type(JsonFieldType.ARRAY),
					fieldWithPath("name").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("content").description("모임 소개글").type(JsonFieldType.STRING),
					fieldWithPath("address").description("모임 장소").type(JsonFieldType.STRING),
					fieldWithPath("firstRegion").description("주소세부; 시/도").type(JsonFieldType.STRING),
					fieldWithPath("secondRegion").description("주소세부; 시/군/구").type(JsonFieldType.STRING),
					fieldWithPath("latitude").description("위도").type(JsonFieldType.STRING),
					fieldWithPath("longitude").description("경도").type(JsonFieldType.STRING),
					fieldWithPath("recruitNumber").description("모집인원").type(JsonFieldType.NUMBER)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("모임 삭제")
	void deleteGroup() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("모임이 삭제 되었습니다.");
		doNothing().when(groupService).deleteGroup(anyLong(), anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/groups/{groupId}", 1L)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("group/delete",
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
	@DisplayName("모임 상태변경 - OPEN")
	void openGroup() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("모임 상태가 'OPEN'으로 변경 되었습니다");
		doNothing().when(groupService).deleteGroup(anyLong(), anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch("/groups/{groupId}/open", 1L)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("group/open",
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
	@DisplayName("모임 상태변경 - CLOSE")
	void closeGroup() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("모임 상태가 'CLOSE'로 변경 되었습니다");
		doNothing().when(groupService).deleteGroup(anyLong(), anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch("/groups/{groupId}/close", 1L)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("group/close",
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