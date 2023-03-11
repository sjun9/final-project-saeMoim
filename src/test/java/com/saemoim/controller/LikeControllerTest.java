package com.saemoim.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.LikeService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = LikeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class LikeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private LikeService likeService;
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
	@DisplayName("게시글 좋아요")
	void addLike() throws Exception {
		// given
		var postId = 1L;
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/posts/{postId}/like", postId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("like/do",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("게시글 id")
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
	@DisplayName("좋아요 취소")
	void deleteLike() throws Exception {
		// given
		var postId = 1L;
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/posts/{postId}/like", postId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("like/cancel",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("게시글 id")
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