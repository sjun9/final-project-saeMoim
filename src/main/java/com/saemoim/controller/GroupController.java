package com.saemoim.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.GroupServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupController {

	private final GroupServiceImpl groupService;

	// 모든 모임 조회
	@GetMapping("/group")
	public Page<GroupResponseDto> getAllGroups(Pageable pageable) {
		return groupService.getAllGroups(pageable);
	}

	// 선택 모임 조회
	@GetMapping("/groups/{groupId}")
	public GroupResponseDto getGroup(@PathVariable Long groupId) {
		return groupService.getGroup(groupId);
	}

	// 특정 카테고리 모임 조회
	@GetMapping("categories/{categoryId}/group")
	public Page<GroupResponseDto> getGroupsByCategory(@PathVariable Long categoryId, Pageable pageable) {
		return groupService.getGroupsByCategory(categoryId, pageable);
	}

	// 내가 만든 모임 조회
	@GetMapping("/leader/group")
	public List<MyGroupResponseDto> getMyGroupsByLeader(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return groupService.getMyGroupsByLeader(userDetails.getId());
	}

	// 참여중인 모임 조회
	@GetMapping("/participant/group")
	public List<MyGroupResponseDto> getMyGroupsByParticipant(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return groupService.getMyGroupsByParticipant(userDetails.getId());
	}

	// 모임 생성
	@PostMapping("/group")
	public GroupResponseDto createGroup(@Validated @RequestBody GroupRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return groupService.createGroup(requestDto, userDetails.getId());
	}

	// 모임 수정
	@PutMapping("/groups/{groupId}")
	public GroupResponseDto updateGroup(@PathVariable Long groupId,
		@Validated @RequestBody GroupRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return groupService.updateGroup(groupId, requestDto, userDetails.getUsername());
	}

	// 모임 삭제
	@DeleteMapping("/groups/{groupId}")
	public ResponseEntity<MessageResponseDto> deleteGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		groupService.deleteGroup(groupId, userDetails.getUsername());
		return new ResponseEntity<>(new MessageResponseDto("삭제 성공"), HttpStatus.OK);
	}

	// 모임 열기
	@PatchMapping("/groups/{groupId}/open")
	public ResponseEntity<MessageResponseDto> openGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		groupService.openGroup(groupId, userDetails.getUsername());
		return new ResponseEntity<>(new MessageResponseDto("모임 Open"), HttpStatus.OK);
	}

	// 모임 닫기
	@PatchMapping("/groups/{groupId}/close")
	public ResponseEntity<MessageResponseDto> closeGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		groupService.closeGroup(groupId, userDetails.getUsername());
		return new ResponseEntity<>(new MessageResponseDto("모임 Close"), HttpStatus.OK);
	}

}
