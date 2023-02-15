package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	// 전체 게시글 조회
	@Transactional(readOnly = true)
	@Override
	public List<PostListResponseDto> getAllPosts() {

		List<Post> postList = postRepository.findAll();


		List<PostListResponseDto> postListResponseDto = new ArrayList<>();
		for (Post post : postList) {
			Long postId = post.getId();
			String title = post.getTitle();
			String username = post.getUser().getUsername();
			LocalDateTime createdAt = post.getCreatedAt();
			LocalDateTime modifiedAt = post.getModifiedAt();

			PostListResponseDto postResponseDto = new PostListResponseDto(postId, title, username, createdAt, modifiedAt);
			postListResponseDto.add(postResponseDto);
		}
		return postListResponseDto;
	}
	// 특정 게시글 조회
	@Transactional(readOnly = true)
	@Override
	public PostResponseDto getPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));

		String title = post.getTitle();
		Long id = post.getUser().getId();
		String username = post.getUser().getUsername();
		String content = post.getContent();
		LocalDateTime createdAt = post.getCreatedAt();
		LocalDateTime modifiedAt = post.getModifiedAt();

		return new PostResponseDto(id, title, username, content, createdAt, modifiedAt);
	}

	@Transactional
	@Override
	public PostResponseDto createPost(Long groupId, PostRequestDto requestDto, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
		Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage()));
		String title = requestDto.getTitle();
		String content = requestDto.getContent();
		Post savedPost = postRepository.save(new Post(group, title, content, user));

		return new PostResponseDto(savedPost);
	}

	@Transactional
	@Override
	public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Long userId) {
		Post savedPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));

		if(savedPost.getUser().getId().equals(userId)){
			String title = requestDto.getTitle();
			String content = requestDto.getContent();

			savedPost.update(title, content);
		}else {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

		return new PostResponseDto(savedPost);
	}

	@Transactional
	@Override
	public void deletePost(Long postId, Long userId) {
		Post savedPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));

		if(savedPost.getUser().getId().equals(userId)){
			postRepository.delete(savedPost);
		}else {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

	}

	@Transactional
	@Override
	public void deletePostByAdmin(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_POST.getMessage())
		);

		postRepository.delete(post);
	}
}
