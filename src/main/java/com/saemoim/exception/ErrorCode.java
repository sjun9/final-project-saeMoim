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
	DUPLICATED_EMAIL("중복된 email 입니다"),
	NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD("비밀번호가 틀렸습니다."),
	INVALID_USER("접근 권한이 없는 사용자입니다."),
	BANNED_USER("정지된 사용자입니다."),

	// 카테고리
	DUPLICATED_CATEGORY("중복된 카테고리 입니다."),
	NOT_EMPTY_CATEGORY("비어있지 않은 카테고리는 삭제할 수 없습니다."),
	NOT_EXIST_CATEGORY("카테고리가 존재하지 않습니다."),
	NOT_PARENT_CATEGORY("기본 카테고리 밑에 생성할 수 있습니다."),

	// 블랙리스트
	DUPLICATED_BLACKLIST("이미 블랙리스트에 등록되어 있습니다."),
	NOT_EXIST_BLACKLIST("해당 블랙리스트가 존재하지 않습니다."),

	// 게시글
	NOT_EXIST_POST("해당 게시글이 존재하지 않습니다."),
	NOT_MATCH_USER("수정/삭제는 작성자만 가능합니다."),

	// 댓글
	NOT_FOUND_COMMENT("해당 댓글을 찾을 수 없습니다."),
	NOT_MATCH_USER("삭제 및 수정은 작성자만 가능 합니다."),

	// 모임
	NOT_FOUND_GROUP("해당 모임은 존재하지 않습니다."),
	INTERNAL_SERVER_ERROR("서버 오류"),
	ALREADY_OPEN("이미 열려있는 모임입니다."),
	ALREADY_CLOSE("이미 닫혀있는 모임입니다."),
	DUPLICATED_APPLICATION("이미 신청되었습니다."),
	NOT_FOUND_APPLICATION("해당 신청은 존재하지 않습니다."),
	NOT_FOUND_REVIEW("해당 후기는 존재하지 않습니다."),
	NOT_FOUND_WISH("찜한 모임이 아닙니다."),
	DUPLICATED_WISH("이미 찜한 모임입니다."),
	NOT_FOUND_PARTICIPANT("해당 참여자는 존재하지 않습니다."),

	NOT_READABLE_JSON("올바르지 않은 JSON 형식입니다."),
	NOT_SUPPORTED_HTTP_MEDIA_TYPE("지원하지 않는 Content-Type 입니다."),
	NOT_ACCEPTABLE_HTTP_MEDIA_TYPE("지원하지 않는 Accept 입니다.");
	private final String message;
}
