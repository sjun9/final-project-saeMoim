package com.saemoim.dto.response;

import java.util.List;

import com.saemoim.domain.Group;
import com.saemoim.domain.Tag;
import com.saemoim.domain.enums.GroupStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupResponseDto implements Comparable<GroupResponseDto> {
	private Long id;
	private List<String> tags;
	private String categoryName;
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
	private Integer wishCount;
	private int recruitNumber;
	private int participantCount;
	private String createdAt;
	private String modifiedAt;

	public GroupResponseDto(Group group) {
		this.id = group.getId();
		this.tags = group.getTags().stream().map(Tag::getName).toList();
		this.categoryName = group.getCategoryName();
		this.groupName = group.getName();
		this.userId = group.getUserId();
		this.username = group.getUsername();
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
		this.createdAt = group.getCreatedAt().toString();
		this.modifiedAt = group.getModifiedAt().toString();
	}

	@Override
	public int compareTo(GroupResponseDto o) {
		if (this.wishCount < o.getWishCount()) {
			return -1;
		} else if (this.wishCount.equals(o.getWishCount())) {
			return 0;
		} else {
			return 1;
		}
	}
}
