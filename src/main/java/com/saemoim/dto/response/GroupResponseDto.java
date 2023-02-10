package com.saemoim.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.saemoim.domain.Group;
import com.saemoim.domain.Review;
import com.saemoim.domain.enums.GroupStatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupResponseDto {
	private Long id;
	private List<String> tagNames;
	private String groupName;
	private Long userId;
	private String username;
	private String content;
	private String address;
	private String firstRegion;
	private String secondRegion;
	private String latitude;
	private String longitude;
	private GroupStatusEnum status;
	private int wishCount;
	private int recruitNumber;
	private int participantCount;
	private final List<ReviewResponseDto> reviews = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public GroupResponseDto(Group group, List<Review> reviews, List<String> tags) {
		this.id = group.getId();
		this.tagNames = tags;
		this.groupName = group.getName();
		this.userId = group.getUser().getId();
		this.username = group.getUser().getUsername();
		this.content = group.getContent();
		this.address = group.getAddress();
		this.firstRegion = group.getFirstRegion();
		this.secondRegion = group.getSecondRegion();
		this.latitude = group.getLatitude();
		this.longitude = group.getLongitude();
		this.status = group.getStatus();
		this.wishCount = group.getWishCount();
		this.recruitNumber = group.getRecruitNumber();
		this.participantCount = group.getParticipantCount();
		this.createdAt = group.getCreatedAt();
		this.modifiedAt = group.getModifiedAt();

		for (Review review : reviews) {
			this.reviews.add(new ReviewResponseDto(review));
		}
	}
}
