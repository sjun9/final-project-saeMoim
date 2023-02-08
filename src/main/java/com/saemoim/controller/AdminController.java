package com.saemoim.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.dto.response.UserResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	// 전체 회원 조회
	@GetMapping("/user")
	public List<UserResponseDto> getAllUsers() {
		return null;
	}

	// 회원 신고 조회
	@GetMapping("/report")
	public List<ReportResponseDto> getReportedUsers() {
		return null;
	}

	// 블랙리스트 목록 조회
	@GetMapping("/blacklist")
	public List<BlackListResponseDto> getBlacklists() {
		return null;
	}

	// 블랙리스트 추가
	@PostMapping("/blacklist/users/{userId}")
	public StatusResponseDto addBlacklist(@PathVariable Long userId) {
		return null;
	}

	// 블랙리스트 해제
	@DeleteMapping("/blacklist/users/{userId}")
	public StatusResponseDto deleteBlacklist(@PathVariable Long userId) {
		return null;
	}

	// 관리자 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public StatusResponseDto deletePostByAdmin(@PathVariable Long postId) {
		return null;
	}

	//관리자 후기 삭제
	@DeleteMapping("/review/{reviewId}")
	public StatusResponseDto deleteReviewByAdmin(@PathVariable Long reviewId) {
		return null;
	}
}
