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
	NOT_FOUND_AUTH_CODE("인증 코드를 다시 받아주세요."),
	NOT_MATCH_CODE("인증 코드가 맞지 않습니다."),
	NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD("비밀번호가 틀렸습니다."),
	INVALID_USER("접근 권한이 없는 사용자입니다."),
	BANNED_USER("정지된 사용자입니다."),

	DUPLICATED_ADMIN("중복된 관리자 아이디 입니다"),
	NOT_FOUND_ADMIN("해당 관리자가 존재하지 않습니다"),
	NOT_ALLOWED_ADMIN("루트 관리자는 삭제할 수 없습니다."),

	// 카테고리
	DUPLICATED_CATEGORY("중복된 카테고리 입니다."),
	NOT_EMPTY_CATEGORY("비어있지 않은 카테고리는 삭제할 수 없습니다."),
	NOT_FOUND_CATEGORY("카테고리가 존재하지 않습니다."),
	NOT_PARENT_CATEGORY("상위 카테고리에 생성할 수 있습니다."),
	NOT_CHILD_CATEGORY("하위 카테고리에 생성할 수 있습니다."),

	// 신고
	DUPLICATED_REPORT("이미 신고한 사용자 입니다."),
	NOT_FOUND_REPORT("신고 내용이 존재하지 않습니다."),

	// 이벤트
	DUPLICATED_EVENT("이미 존재하는 이벤트 입니다."),
	NOT_FOUND_EVENT("해당 이벤트가 존재하지 않습니다."),
	DUPLICATED_EVENT_USER("이미 신청한 이벤트 입니다."),
	NOT_OVER_START_TIME("이벤트 시작 시간 전 입니다"),
	FINISHED_EVENT("이벤트가 종료 되었습니다."),

	// 선물
	NOT_FOUND_GIFT("해당 당첨 내역이 존재하지 않습니다."),

	// 후기
	DUPLICATED_REVIEW("이미 후기를 등록하였습니다."),

	// 블랙리스트
	DUPLICATED_BLACKLIST("이미 블랙리스트에 등록되어 있습니다."),
	NOT_FOUND_BLACKLIST("해당 블랙리스트가 존재하지 않습니다."),

	// 게시글
	NOT_FOUND_POST("해당 게시글이 존재하지 않습니다."),
	NOT_MATCH_USER("수정/삭제는 작성자만 가능합니다."),

	// 댓글
	NOT_FOUND_COMMENT("해당 댓글을 찾을 수 없습니다."),

	// 좋아요
	DUPLICATED_LIKE("이미 '좋아요'를 누르셨습니다."),
	ALREADY_DELETED("이미 '좋아요'가 취소되었습니다."),

	// 파일 업로드
	NOT_IMAGE_FILE("이미지 파일이 아닙니다."),
	EMPTY_FILE("파일이 없습니다."),
	FAIL_IMAGE_UPLOAD("이미지 업로드에 실패 했습니다."),

	// 모임
	NOT_FOUND_GROUP("해당 모임은 존재하지 않습니다."),
	DUPLICATED_GROUP_NAME("존재하는 모임명입니다."),
	INTERNAL_SERVER_ERROR("서버 오류"),
	ALREADY_OPEN("이미 열려있는 모임입니다."),
	ALREADY_CLOSE("이미 닫혀있는 모임입니다."),
	DUPLICATED_APPLICATION("이미 신청되었습니다."),
	NOT_FOUND_APPLICATION("해당 신청은 존재하지 않습니다."),
	ALREADY_PROCESSED("이미 처리된 신청입니다."),
	NOT_FOUND_REVIEW("해당 후기는 존재하지 않습니다."),
	NOT_FOUND_WISH("찜한 모임이 아닙니다."),
	DUPLICATED_WISH("이미 찜한 모임입니다."),
	DUPLICATED_PARTICIPANT("이미 존재하는 참여자입니다."),
	NOT_FOUND_PARTICIPANT("해당 참여자는 존재하지 않습니다."),

	NOT_READABLE_JSON("올바르지 않은 JSON 형식입니다."),
	NOT_SUPPORTED_HTTP_MEDIA_TYPE("지원하지 않는 Content-Type 입니다."),
	NOT_ACCEPTABLE_HTTP_MEDIA_TYPE("지원하지 않는 Accept 입니다."),
	UNAUTHORIZED_TOKEN("재로그인이 필요합니다.");
	private final String message;
}
