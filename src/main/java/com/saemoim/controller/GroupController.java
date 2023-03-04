package com.saemoim.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupController {

	private final GroupService groupService;

	// 모든 모임 조회
	@GetMapping("/group")
	public ResponseEntity<GenericsResponseDto> getAllGroups(Pageable pageable) {
		return ResponseEntity.ok().body(new GenericsResponseDto<>(groupService.getAllGroups(pageable)));
	}

	// 선택 모임 조회
	@GetMapping("/groups/{groupId}")
	public ResponseEntity<GroupResponseDto> getGroup(@PathVariable Long groupId) {
		return ResponseEntity.ok().body(groupService.getGroup(groupId));
	}

	// 인기 모임 조회
	@GetMapping("/group/popular")
	public ResponseEntity<GenericsResponseDto> getGroupByPopularity() {
		return ResponseEntity.ok().body(new GenericsResponseDto(groupService.getGroupByPopularity()));
	}

	//특정 카테고리 모임 조회
	@GetMapping("/group/categories/{categoryId}")
	public ResponseEntity<GenericsResponseDto> getGroupsByCategoryAndStatus(@PathVariable Long categoryId,
		@RequestParam String status, Pageable pageable) {
		return ResponseEntity.ok().body(new GenericsResponseDto(
			groupService.getGroupsByCategoryAndStatus(categoryId, status, pageable)));
	}

	// 특정 태그 모임 조회
	@GetMapping("/group/tag")
	public ResponseEntity<GenericsResponseDto> getGroupsByTag(@RequestParam String tagName, Pageable pageable) {
		return ResponseEntity.ok()
			.body(new GenericsResponseDto(groupService.searchGroupsByTag(tagName, pageable)));
	}

	// 모임 이름으로 검색
	@GetMapping("/group/name")
	public ResponseEntity<GenericsResponseDto> searchGroups(@RequestParam String groupName, Pageable pageable) {
		return ResponseEntity.ok()
			.body(new GenericsResponseDto(groupService.searchGroups(groupName, pageable)));
	}

	// 내가 만든 모임 조회
	@GetMapping("/leader/group")
	public ResponseEntity<GenericsResponseDto> getMyGroupsByLeader(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(new GenericsResponseDto(groupService.getMyGroupsByLeader(userDetails.getId())));
	}

	// 참여중인 모임 조회
	@GetMapping("/participant/group")
	public ResponseEntity<GenericsResponseDto> getMyGroupsByParticipant(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok()
			.body(new GenericsResponseDto(groupService.getMyGroupsByParticipant(userDetails.getId())));
	}

	// 모임 생성
	@PostMapping(value = "/group", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public ResponseEntity<GroupResponseDto> createGroup(
		@Validated @RequestPart("requestDto") GroupRequestDto requestDto,
		@RequestPart(required = false ,name = "img") MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(groupService.createGroup(requestDto, userDetails.getId(), multipartFile));
	}

	// 모임 수정
	@PutMapping(value = "/groups/{groupId}" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<GroupResponseDto> updateGroup(@PathVariable Long groupId,
		@Validated @RequestPart GroupRequestDto requestDto,
		@RequestPart(required = false ,name = "img") MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		System.out.println("aaaaaa" + requestDto.getName()); //
		return ResponseEntity.ok().body(groupService.updateGroup(groupId, requestDto, userDetails.getId(),multipartFile));
	}

	// 모임 삭제
	@DeleteMapping("/groups/{groupId}")
	public ResponseEntity<GenericsResponseDto> deleteGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		groupService.deleteGroup(groupId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("모임이 삭제 되었습니다."));
	}

	// 모임 열기
	@PatchMapping("/groups/{groupId}/open")
	public ResponseEntity<GenericsResponseDto> openGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		groupService.openGroup(groupId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("모임 상태가 'OPEN'으로 변경 되었습니다"));
	}

	// 모임 닫기
	@PatchMapping("/groups/{groupId}/close")
	public ResponseEntity<GenericsResponseDto> closeGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		groupService.closeGroup(groupId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("모임 상태가 'CLOSE'로 변경 되었습니다"));
	}

}
