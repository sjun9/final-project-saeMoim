package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.WishService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WishController {

	private final WishService wishService;

	// 모임 즐겨찾기 조회
	@Secured(UserRoleEnum.Authority.USER)
	@GetMapping("/group/wish")
	public ResponseEntity<GenericsResponseDto> getWishGroups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(new GenericsResponseDto(wishService.getWishGroups(userDetails.getId())));
	}

	// 모임 즐겨찾기 추가
	@PostMapping("/groups/{groupId}/wish")
	public ResponseEntity<GenericsResponseDto> wishGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		wishService.addWishGroup(groupId, userDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("모임 찜하기 완료"));
	}

	// 모임 즐겨찾기 해제
	@DeleteMapping("/groups/{groupId}/wish")
	public ResponseEntity<GenericsResponseDto> deleteWishGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		wishService.deleteWishGroup(groupId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("모임 찜하기 취소 완료"));
	}
}
