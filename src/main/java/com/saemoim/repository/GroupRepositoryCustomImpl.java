package com.saemoim.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.saemoim.domain.Group;
import com.saemoim.domain.QCategory;
import com.saemoim.domain.QGroup;
import com.saemoim.domain.QUser;
import com.saemoim.domain.enums.GroupStatusEnum;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<Group> findAllByOrderByGroupIdDesc(Long groupId, Pageable pageable) {
		List<Group> content = jpaQueryFactory.selectFrom(QGroup.group)
			.join(QGroup.group.user, QUser.user)
			.fetchJoin()
			.join(QGroup.group.category, QCategory.category)
			.fetchJoin()
			.distinct()
			.where(ltGroupId(groupId))
			.orderBy(QGroup.group.id.desc())
			.limit(pageable.getPageSize())
			.fetch();

		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageable, hasNext);
	}

	@Override
	public Slice<Group> findByCategoryAndStatusByOrderByCreateAtDesc(Long categoryId, String status,
		Pageable pageable) {
		List<Group> content = jpaQueryFactory.selectFrom(QGroup.group)
			.join(QGroup.group.user, QUser.user)
			.fetchJoin()
			.join(QGroup.group.category, QCategory.category)
			.fetchJoin()
			.distinct()
			.where(eqCategoryId(categoryId), eqStatus(status))
			.orderBy(QGroup.group.createdAt.desc(), QGroup.group.id.desc())
			.limit(pageable.getPageSize())
			.fetch();

		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageable, hasNext);
	}

	private BooleanExpression ltGroupId(Long groupId) {
		if (groupId == null) {
			return null;
		}
		return QGroup.group.id.lt(groupId);
	}

	private BooleanExpression eqCategoryId(Long categoryId) {
		if (categoryId.equals(0L)) {
			return null;
		}
		return QGroup.group.category.id.eq(categoryId);
	}

	private BooleanExpression eqStatus(String status) {
		if (status.equals("")) {
			return null;
		}
		return QGroup.group.status.eq(GroupStatusEnum.valueOf(status));
	}
}
