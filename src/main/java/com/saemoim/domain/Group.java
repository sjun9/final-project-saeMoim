package com.saemoim.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.request.GroupRequestDto;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "sae_group")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group extends TimeStamped implements Comparable<Group> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Tag> tags = new ArrayList<>();

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
	private int wishCount = 0;

	@Column(nullable = false)
	private int recruitNumber;

	@Column(nullable = false)
	private int participantCount = 0;

	@Column(nullable = false)
	private String imagePath = "/resources/static/images/bird.png";

	@Column(nullable = false)
	private int views = 0;

	public Group(GroupRequestDto request, Category category, User user, String imgPath) {
		this.user = user;
		this.category = category;
		request.getTagNames().forEach(t -> this.tags.add(new Tag(t, this)));
		this.name = request.getName();
		this.content = request.getContent();
		this.address = request.getAddress();
		this.firstRegion = request.getFirstRegion();
		this.secondRegion = request.getSecondRegion();
		this.latitude = request.getLatitude();
		this.longitude = request.getLongitude();
		this.recruitNumber = request.getRecruitNumber();
		this.imagePath = imgPath;
	}

	public Group(GroupRequestDto request, Category category, User user) {
		this.user = user;
		this.category = category;
		request.getTagNames().forEach(t -> this.tags.add(new Tag(t, this)));
		this.name = request.getName();
		this.content = request.getContent();
		this.address = request.getAddress();
		this.firstRegion = request.getFirstRegion();
		this.secondRegion = request.getSecondRegion();
		this.latitude = request.getLatitude();
		this.longitude = request.getLongitude();
		this.recruitNumber = request.getRecruitNumber();
	}

	public void update(GroupRequestDto request, Category category) {
		this.category = category;
		this.tags.clear();
		request.getTagNames().forEach(t -> this.tags.add(new Tag(t, this)));
		this.name = request.getName();
		this.content = request.getContent();
		this.address = request.getAddress();
		this.firstRegion = request.getFirstRegion();
		this.secondRegion = request.getSecondRegion();
		this.latitude = request.getLatitude();
		this.longitude = request.getLongitude();
		this.recruitNumber = request.getRecruitNumber();
	}

	public void update(GroupRequestDto request, Category category, String imagePath) {
		this.category = category;
		this.tags.clear();
		request.getTagNames().forEach(t -> this.tags.add(new Tag(t, this)));
		this.name = request.getName();
		this.content = request.getContent();
		this.address = request.getAddress();
		this.firstRegion = request.getFirstRegion();
		this.secondRegion = request.getSecondRegion();
		this.latitude = request.getLatitude();
		this.longitude = request.getLongitude();
		this.recruitNumber = request.getRecruitNumber();
		this.imagePath = imagePath;
	}

	public String getUsername() {
		return this.user.getUsername();
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public String getCategoryName() {
		return this.category.getName();
	}

	public boolean isLeader(Long userId) {
		return this.getUserId().equals(userId);
	}

	public void updateStatusToOpen() {
		this.status = GroupStatusEnum.OPEN;
	}

	public void updateStatusToClose() {
		this.status = GroupStatusEnum.CLOSE;
	}

	public void addParticipantCount() {
		this.participantCount++;
	}

	public void subtractParticipantCount() {
		this.participantCount--;
	}

	public void addWishCount() {
		this.wishCount++;
	}

	public void subtractWishCount() {
		this.wishCount--;
	}

	public void addViews() {
		this.views++;
	}

	public boolean isOpen() {
		return this.status.equals(GroupStatusEnum.OPEN);
	}

	public boolean isClose() {
		return this.status.equals(GroupStatusEnum.CLOSE);
	}

	@Override
	public int compareTo(Group o) {
		Integer x = (this.wishCount * 10) + this.views;
		Integer y = (o.wishCount * 10) + o.views;
		if (x < y) {
			return -1;
		} else if (x.equals(y)) {
			return 0;
		} else {
			return 1;
		}
	}
}
