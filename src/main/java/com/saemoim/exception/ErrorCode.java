package com.saemoim.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 토큰
	INVALID_AUTH_TOKEN("권한 정보가 없는 토큰입니다"),
	INVALID_TOKEN("토큰이 유효하지 않습니다."),

	// 유저
	DUPLICATED_USERNAME("중복된 username 입니다"),
	NOT_FOUND_USER("회원을 찾을 수 없습니다."),
	INVALID_PASSWORD("비밀번호가 틀렸습니다."),
	INVALID_USER("접근 권한이 없는 사용자입니다."),

	// requesst
	NOT_READABLE_JSON("올바르지 않은 JSON 형식입니다."),
	NOT_SUPPORTED_HTTP_MEDIA_TYPE("지원하지 않는 Content-Type 입니다."),
	NOT_ACCEPTABLE_HTTP_MEDIA_TYPE("지원하지 않는 Accept 입니다."),

	// 카테고리
	NOT_FOUND_CATEGORY("해당 카테고리는 존재하지 않습니다."),
	NOT_CHILD_CATEGORY("하위 카테고리를 선택해주세요."),

	// 모임
	NOT_FOUND_GROUP("해당 모임은 존재하지 않습니다."),

	INTERNAL_SERVER_ERROR("서버 오류");

	private final String message;
}
