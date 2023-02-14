package com.saemoim.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Comment;
import com.saemoim.domain.Post;
import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.dto.response.PostListResponseDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CommentRepository;
import com.saemoim.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

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

		List<Comment> comments = commentRepository.findAllByPostId(postId);
		List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

		for (Comment comment : comments) {
			String writer = comment.getUser().getUsername();
			String comment1 = comment.getComment();
			LocalDateTime createdAt1 = comment.getCreatedAt();
			LocalDateTime modifiedAt1 = comment.getModifiedAt();

			CommentResponseDto commentResponseDto = new CommentResponseDto(writer, comment1, createdAt1, modifiedAt1);
			commentResponseDtos.add(commentResponseDto);
		}
		return new PostResponseDto(id, title, username, content, createdAt, modifiedAt, commentResponseDtos);
	}

	@Transactional
	@Override
	public PostResponseDto createPost(Long groupId, PostRequestDto requestDto, String username) {
		String title = requestDto.getTitle();
		String content = requestDto.getContent();

		postRepository.save(new Post(groupId, title, content, username));
		return null;
	}

	@Transactional
	@Override
	public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public void deletePost(Long postId, String username) {

	}
}
