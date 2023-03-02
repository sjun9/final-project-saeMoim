package com.saemoim.controller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.saemoim.domain.Category;
import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.CategoryServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = CategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CategoryControllerTest {

	@MockBean
	private CategoryServiceImpl categoryService;

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
	@DisplayName("카테고리 조회")
	void getCategories() throws Exception {
		//given
		Category category = Category.builder().id(1L).name("여행").build();
		Category childCategory = Category.builder().id(2L)
			.parentId(1L)
			.name("부산")
			.build();
		List<Category> childList = new ArrayList<>();
		childList.add(childCategory);

		CategoryResponseDto responseDto = new CategoryResponseDto(category, childList);
		List<CategoryResponseDto> responseDtoList = new ArrayList<>();
		responseDtoList.add(responseDto);

		when(categoryService.getCategories()).thenReturn(responseDtoList);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/category"));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$['data'][0]['name']", responseDto.getName()).exists())
			.andDo(document("category/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					subsectionWithPath("data").description("카테고리"),
					fieldWithPath("data.[].id").description("상위 카테고리 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].name").description("상위 카테고리 이름").type(JsonFieldType.STRING),
					subsectionWithPath("data.[].categories").description("하위 카테고리 리스트")
				)
			));
	}

	@Test
	@DisplayName("부모 카테고리 생성")
	void createCategory() throws Exception {
		//given
		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();
		GenericsResponseDto responseDto = new GenericsResponseDto(requestDto.getName() + " 카테고리 생성 완료");
		doNothing().when(categoryService).createParentCategory(any(CategoryRequestDto.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/admin/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto))
				.header("Authorization", "Bearer adminAccessToken"));

		//then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("category/createParent",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("어드민계정 엑세스토큰")
				),
				requestFields(
					fieldWithPath("name").description("카테고리 이름").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("자식 카테고리 생성")
	void createChildCategory() throws Exception {
		//given
		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();
		GenericsResponseDto responseDto = new GenericsResponseDto(requestDto.getName() + " 카테고리 생성 완료");
		doNothing().when(categoryService).createChildCategory(anyLong(), any(CategoryRequestDto.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/admin/categories/{parentId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto))
				.header("Authorization", "Bearer adminAccessToken"));

		//then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("category/createChild",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("parentId").description("상위 카테고리 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("어드민계정 엑세스토큰")
				),
				requestFields(
					fieldWithPath("name").description("카테고리 이름").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("카테고리 수정")
	void updateCategory() throws Exception {
		//given
		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();
		GenericsResponseDto responseDto = new GenericsResponseDto(requestDto.getName() + " 카테고리 수정 완료");
		doNothing().when(categoryService).updateCategory(anyLong(), any(CategoryRequestDto.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/admin/categories/{categoryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto))
				.header("Authorization", "Bearer adminAccessToken"));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("category/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("categoryId").description("카테고리 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("어드민계정 엑세스토큰")
				),
				requestFields(
					fieldWithPath("name").description("카테고리 이름").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("카테고리 삭제")
	void deleteCategory() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("카테고리 삭제 완료");
		doNothing().when(categoryService).deleteCategory(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/admin/categories/{categoryId}", 1L)
				.header("Authorization", "Bearer adminAccessToken"));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data", responseDto.getData()).exists())
			.andDo(document("category/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("categoryId").description("카테고리 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("어드민계정 엑세스토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}
}