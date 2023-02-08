package com.saemoim.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.saemoim.domain.enums.GroupStatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupResponseDto {
	private Long id;
	private String groupName;
	private Long userId;
	private String username;
	private String content;
	private String location;
	private GroupStatusEnum status;
	private int wishCount;
	private int recruitNumber;
	private int participantCount;
	private List<ReviewResponseDto> reviews;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
