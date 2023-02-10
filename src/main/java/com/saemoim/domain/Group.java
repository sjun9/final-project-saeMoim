package com.saemoim.domain;

import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.request.GroupRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "sae_group")
@Getter
@NoArgsConstructor
public class Group extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String firstRegion;
	@Column(nullable = false)
	private String secondRegion;

	@Column(nullable = false)
	private String latitude;

	@Column(nullable = false)
	private String longitude;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GroupStatusEnum status = GroupStatusEnum.OPEN;

	@Column(nullable = false)
	private Integer wishCount = 0;

	@Column(nullable = false)
	private Integer recruitNumber = 0;
	@Column(nullable = false)
	private Integer participantCount = 0;

	public Group(GroupRequestDto request, Category category, User user) {
		this.user = user;
		this.category = category;
		this.name = request.getName();
		this.content = request.getContent();
		this.address = request.getAddress();
		this.firstRegion = request.getFirstRegion();
		this.secondRegion = request.getSecondRegion();
		this.latitude = request.getLatitude();
		this.longitude = request.getLongitude();
		this.recruitNumber = request.getRecruitNumber();
	}

}
