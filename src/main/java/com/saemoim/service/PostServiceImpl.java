package com.saemoim.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.domain.Group;
import com.saemoim.domain.Post;
import com.saemoim.domain.User;
import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.fileUpload.AWSS3Uploader;
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
	private final AWSS3Uploader awsS3Uploader;
	private final String dirName = "post";

	// 모임 전체 게시글 조회
	@Transactional(readOnly = true)
	@Override
	public Page<PostResponseDto> getAllPostsByGroup(Long group_id, Pageable pageable, Long userId) {
		Page<Post> postList = postRepository.findAllByGroup_IdOrderByCreatedAtDesc(group_id, pageable);
		return postList.map(post -> {
			Long id = post.getId();
			Long postUserId = post.getUserId();
			String title = post.getTitle();
			String username = post.getUsername();
			String content = post.getContent();
			LocalDateTime createdAt = post.getCreatedAt();
			LocalDateTime modifiedAt = post.getModifiedAt();
			int likeCount = post.getLikeCount();
			String imagePath = post.getImagePath();
			boolean isLikeChecked = likeRepository.existsByPost_IdAndUserId(id, userId);

			return PostResponseDto.builder()
				.id(id)
				.userId(postUserId)
				.title(title)
				.username(username)
				.content(content)
				.createdAt(createdAt)
				.modifiedAt(modifiedAt)
				.likeCount(likeCount)
				.isLikeChecked(isLikeChecked)
				.imagePath(imagePath)
				.build();
		});
	}

	// 특정 게시글 조회
	@Transactional(readOnly = true)
	@Override
	public PostResponseDto getPost(Long postId, Long userId) {
		Post post = _getPostById(postId);
		Long id = post.getId();
		Long postUserId = post.getUserId();
		String title = post.getTitle();
		String username = post.getUsername();
		String content = post.getContent();
		LocalDateTime createdAt = post.getCreatedAt();
		LocalDateTime modifiedAt = post.getModifiedAt();
		int likeCount = post.getLikeCount();
		String imagePath = post.getImagePath();
		boolean isLikeChecked = likeRepository.existsByPost_IdAndUserId(id, userId);

		return PostResponseDto.builder()
			.id(id)
			.userId(postUserId)
			.title(title)
			.username(username)
			.content(content)
			.createdAt(createdAt)
			.modifiedAt(modifiedAt)
			.likeCount(likeCount)
			.isLikeChecked(isLikeChecked)
			.imagePath(imagePath)
			.build();
	}

	@Transactional
	@Override
	public void createPost(Long groupId, PostRequestDto requestDto, Long userId, MultipartFile multipartFile) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);

		String imagePath;
		Post post;

		if (multipartFile == null) {
			post = new Post(group, requestDto.getTitle(), requestDto.getContent(), user);
		} else {
			try {
				imagePath = awsS3Uploader.upload(multipartFile, dirName);
				post = new Post(group, requestDto.getTitle(), requestDto.getContent(), user, imagePath);
			} catch (IOException e) {
				throw new IllegalArgumentException(ErrorCode.FAIL_IMAGE_UPLOAD.getMessage());
			}
		}
		postRepository.save(post);
	}

	@Transactional
	@Override
	public void updatePost(Long postId, PostRequestDto requestDto, Long userId, MultipartFile multipartFile) {
		Post savedPost = _getPostById(postId);

		if (!savedPost.isWriter(userId)) {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}
		String imagePath;
		if (multipartFile == null) {
			savedPost.update(requestDto.getTitle(), requestDto.getContent());
		} else {
			try {
				imagePath = awsS3Uploader.upload(multipartFile, dirName);
				awsS3Uploader.delete(savedPost.getImagePath());
				savedPost.update(requestDto.getTitle(), requestDto.getContent(), imagePath);
			} catch (IOException e) {
				throw new IllegalArgumentException(ErrorCode.FAIL_IMAGE_UPLOAD.getMessage());
			}
		}
		postRepository.save(savedPost);
	}

	@Transactional
	@Override
	public void deletePost(Long postId, Long userId) {
		Post savedPost = _getPostById(postId);

		if (!savedPost.isWriter(userId)) {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

		awsS3Uploader.delete(savedPost.getImagePath());
		postRepository.delete(savedPost);
	}

	@Transactional
	@Override
	public void deletePostByAdmin(Long postId) {
		Post post = _getPostById(postId);

		awsS3Uploader.delete(post.getImagePath());
		postRepository.delete(post);
	}

	private Post _getPostById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
	}
}
