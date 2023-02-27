package com.saemoim.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Group;
import com.saemoim.domain.Post;
import com.saemoim.domain.User;
import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.LikeRepository;
import com.saemoim.repository.PostRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final LikeRepository likeRepository;

	// 모임 전체 게시글 조회
	@Transactional(readOnly = true)
	@Override
	public Page<PostResponseDto> getAllPostsByGroup(Long group_id, Pageable pageable) {
		List<PostResponseDto> list = postRepository.findAllByGroup_Id(group_id)
			.stream()
			.map(PostResponseDto::new)
			.toList();
		return new PageImpl<>(list, pageable, list.size());
	}

	// 특정 게시글 조회
	@Transactional(readOnly = true)
	@Override
	public PostResponseDto getPost(Long postId, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));

		String title = post.getTitle();
		Long id = post.getUserId();
		String username = post.getUser().getUsername();
		String content = post.getContent();
		LocalDateTime createdAt = post.getCreatedAt();
		LocalDateTime modifiedAt = post.getModifiedAt();
		int likeCount = post.getLikeCount();

		boolean isLikeChecked = likeRepository.existsByPost_IdAndUserId(postId, userId);

		return PostResponseDto.builder()
			.title(title)
			.id(id)
			.username(username)
			.content(content)
			.createdAt(createdAt)
			.modifiedAt(modifiedAt)
			.likeCount(likeCount)
			.isLikeChecked(isLikeChecked)
			.build();
	}

	@Transactional
	@Override
	public PostResponseDto createPost(Long groupId, PostRequestDto requestDto, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage()));
		String title = requestDto.getTitle();
		String content = requestDto.getContent();
		Post savedPost = postRepository.save(new Post(group, title, content, user));
		return new PostResponseDto(savedPost);
	}

	@Transactional
	@Override
	public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Long userId) {
		Post savedPost = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));

		if (savedPost.isWriter(userId)) {
			String title = requestDto.getTitle();
			String content = requestDto.getContent();

			savedPost.update(title, content);
		} else {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

		return new PostResponseDto(savedPost);
	}

	@Transactional
	@Override
	public void deletePost(Long postId, Long userId) {
		Post savedPost = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));

		if (savedPost.isWriter(userId)) {
			postRepository.delete(savedPost);

		} else {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

	}

	@Transactional
	@Override
	public void deletePostByAdmin(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
		);

		postRepository.delete(post);
	}
}
