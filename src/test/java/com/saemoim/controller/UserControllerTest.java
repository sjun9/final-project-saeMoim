package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.UsernameRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.UserServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserServiceImpl userService;
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
	@DisplayName("회원 가입")
	void signUp() throws Exception {
		//given
		SignUpRequestDto requestDto = SignUpRequestDto.builder()
			.username("nickName")
			.password("Password1!")
			.email("email@naver.com")
			.build();
		doNothing().when(userService).signUp(any(SignUpRequestDto.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-up")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));

		//then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("data").value("회원가입이 완료 되었습니다."))
			.andDo(document("user/sign-up",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("이메일").type(JsonFieldType.STRING),
					fieldWithPath("password").description("비밀번호").type(JsonFieldType.STRING),
					fieldWithPath("username").description("닉네임").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("이메일 중복 검사")
	void checkEmailDuplication() throws Exception {
		//given
		EmailRequestDto requestDto = EmailRequestDto.builder()
			.email("email@naver.com")
			.build();
		doNothing().when(userService).checkEmailDuplication(any(EmailRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-up/email")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data").value("이메일 중복 검사가 완료 되었습니다."))
			.andDo(document("user/email-check",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("이메일").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("닉네임 중복 검사")
	void checkUsernameDuplication() throws Exception {
		//given
		UsernameRequestDto requestDto = UsernameRequestDto.builder()
			.username("nickName")
			.build();
		doNothing().when(userService).checkUsernameDuplication(any(UsernameRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-up/username")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data").value("이름 중복 검사가 완료 되었습니다."))
			.andDo(document("user/username-check",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("username").description("닉네임").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("로그인")
	@WithCustomMockUser
	void signIn() throws Exception {
		//given
		TokenResponseDto responseDto = mock(TokenResponseDto.class);
		SignInRequestDto requestDto = SignInRequestDto.builder()
			.email("email@naver.com")
			.password("Pass1234!")
			.build();

		when(responseDto.getAccessToken()).thenReturn("Bearer accessToken");
		when(responseDto.getRefreshToken()).thenReturn("Bearer refreshToken");
		when(userService.signIn(any(SignInRequestDto.class))).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-in")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string("Authorization", "Bearer accessToken"))
			.andExpect(header().string("Refresh_Token", "Bearer refreshToken"))
			.andExpect(jsonPath("data").value("로그인이 완료 되었습니다."))
			.andDo(document("user/login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("이메일").type(JsonFieldType.STRING),
					fieldWithPath("password").description("비밀번호").type(JsonFieldType.STRING)
				),
				responseHeaders(
					headerWithName("Authorization").description("엑세스토큰"),
					headerWithName("Refresh_Token").description("리프레시토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("로그아웃")
	void logout() throws Exception {
		//given
		doNothing().when(userService).logout(anyString());

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/log-out")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.header(JwtUtil.REFRESH_TOKEN_HEADER, "Bearer refreshToken")
			.contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string("Authorization", ""))
			.andExpect(jsonPath("data").value("로그아웃이 완료 되었습니다."))
			.andDo(document("user/logout",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰"),
					headerWithName("Refresh_Token").description("리프레시토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("회원 탈퇴")
	void withdraw() throws Exception {
		//given
		WithdrawRequestDto requestDto = WithdrawRequestDto.builder()
			.password("Pass1234!")
			.build();

		doNothing().when(userService).withdraw(requestDto, 1L, "Bearer refreshToken");
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/withdrawal")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.header(JwtUtil.REFRESH_TOKEN_HEADER, "Bearer refreshToken")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string("Authorization", ""))
			.andExpect(jsonPath("data").value("회원탈퇴가 완료 되었습니다."))
			.andDo(document("user/withdrawal",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰"),
					headerWithName("Refresh_Token").description("리프레시토큰")
				),
				requestFields(
					fieldWithPath("password").description("비밀번호").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	@DisplayName("전체 회원 조회")
	void getAllUsers() throws Exception {
		//given
		List<UserResponseDto> list = new ArrayList<>();
		UserResponseDto responseDto = UserResponseDto.builder()
			.id(1L)
			.banCount(0)
			.email("email@naver.com")
			.username("nickName")
			.createdAt(LocalDateTime.now())
			.build();

		list.add(responseDto);

		when(userService.getAllUsers()).thenReturn(list);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/user")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("관리자엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("유저목록"),
					fieldWithPath("data.[].id").description("유저 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].email").description("유저 이메일").type(JsonFieldType.STRING),
					fieldWithPath("data.[].username").description("유저 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].banCount").description("정지당한 횟수").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].createdAt").description("가입일자").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("회원 프로필 조회")
	void getProfile() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("안녕하시렵니까")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("닉넹미")
			.imagePath("imgPath")
			.build();

		ProfileResponseDto response = new ProfileResponseDto(user);

		when(userService.getProfile(anyLong())).thenReturn(response);
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/profile/users/{userId}", 1L)
				.header("Authorization", "Bearer accessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/profile",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("userId").description("유저 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("id").description("유저 id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("유저 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("content").description("소개글").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("이미지 저장경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("내 정보 조회")
	void getMyProfile() throws Exception {
		// given
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("안녕하시렵니까")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("닉넹미")
			.imagePath("imgPath")
			.build();
		ProfileResponseDto response = new ProfileResponseDto(user);

		when(userService.getMyProfile(anyLong())).thenReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/profile")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/myProfile",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("id").description("유저 id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("유저 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("content").description("소개글").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("이미지 저장경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("사용자정보조회")
	void getUserInfo() throws Exception {
		// given
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("안녕하시렵니까")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("닉넹미")
			.imagePath("imgPath")
			.build();
		ProfileResponseDto response = new ProfileResponseDto(user);

		when(userService.getProfile(anyLong())).thenReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/user")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/user-info",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("id").description("유저 id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("유저 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("content").description("소개글").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("이미지 저장경로").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("내 정보 수정")
	void updateProfile() throws Exception {
		// given
		MockMultipartFile image = new MockMultipartFile("img", "image.png", "image/png",
			"<<png data>>".getBytes());
		MockMultipartFile request = new MockMultipartFile("requestDto", "",
			"application/json", "{ \"content\": \"1.0\"}".getBytes());
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("안녕하시렵니까")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("닉넹미")
			.imagePath("imgPath")
			.build();
		ProfileResponseDto responseDto = new ProfileResponseDto(user);
		when(userService.updateProfile(anyLong(), any(ProfileRequestDto.class), any(MultipartFile.class))).thenReturn(
			responseDto);
		// when
		MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder)multipart(
			HttpMethod.PUT, "/profile")
			.requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/profile");
		ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder
			.file(image).file(request)
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/update-profile",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestPartFields("requestDto",
					fieldWithPath("content").description("소개글").type(JsonFieldType.STRING)
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("id").description("유저 id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("유저 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("content").description("소개글").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("이미지 저장경로").type(JsonFieldType.STRING)
				)
			));

	}

	@Test
	@WithCustomMockUser
	@DisplayName("리프레쉬 토큰 재발급")
	void reissueSuccess() throws Exception {
		// given
		String accessToken = "Bearer accessToken";
		String refreshToken = "Bearer refreshToken";
		TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
		when(userService.reissueToken(anyString(), anyString())).thenReturn(tokenResponseDto);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/reissue")
			.header(JwtUtil.AUTHORIZATION_HEADER, accessToken)
			.header(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string(JwtUtil.AUTHORIZATION_HEADER, accessToken))
			.andExpect(header().string(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken))
			.andDo(document("user/reissue",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰"),
					headerWithName("Refresh_Token").description("리프레시토큰")
				),
				responseHeaders(
					headerWithName("Authorization").description("엑세스토큰"),
					headerWithName("Refresh_Token").description("리프레시토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

}