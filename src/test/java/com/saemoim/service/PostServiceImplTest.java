package com.saemoim.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.domain.Group;
import com.saemoim.domain.Post;
import com.saemoim.domain.User;
import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.LikeRepository;
import com.saemoim.repository.PostRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private LikeRepository likeRepository;
	@Mock
	private AWSS3Uploader awss3Uploader;
	@InjectMocks
	private PostServiceImpl postService;

	@Test
	@DisplayName("게시글 생성")
	void createPost() throws IOException {
		//given
		Long groupId = 1L;
		Long userId = 1L;
		PostRequestDto requestDto = mock(PostRequestDto.class);
		User user = mock(User.class);
		Group group = mock(Group.class);
		MultipartFile multipartFile = mock(MultipartFile.class);
		String imagePath = "imagePath";
		String dirName = "post";

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(requestDto.getTitle()).thenReturn("title");
		when(requestDto.getContent()).thenReturn("content");
		when(awss3Uploader.upload(multipartFile, dirName)).thenReturn(imagePath);
		//when
		postService.createPost(groupId, requestDto, userId, multipartFile);
		//then
		verify(postRepository).save(any(Post.class));
	}

	@Test
	@DisplayName("게시글 수정")
	void updatePost() throws IOException {
		//given
		Long postId = 1L;
		Long userId = 1L;
		PostRequestDto requestDto = mock(PostRequestDto.class);
		Post post = mock(Post.class);
		MultipartFile multipartFile = mock(MultipartFile.class);
		String imagePath = "imagePath";
		String getImagePath = "getImagePath";
		String dirName = "post";

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(post.isWriter(userId)).thenReturn(true);
		when(requestDto.getTitle()).thenReturn("title");
		when(requestDto.getContent()).thenReturn("content");
		when(awss3Uploader.upload(multipartFile, dirName)).thenReturn(imagePath);
		when(post.getImagePath()).thenReturn(getImagePath);
		doNothing().when(awss3Uploader).delete(getImagePath);
		//when
		postService.updatePost(postId, requestDto, userId, multipartFile);
		//then
		verify(postRepository).save(post);
	}

	@Test
	void deletePost() {
		//given
		Long postId = 1L;
		Long userId = 1L;
		Post post = mock(Post.class);
		String getImagePath = "getImagePath";

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(post.isWriter(userId)).thenReturn(true);
		when(post.getImagePath()).thenReturn(getImagePath);
		doNothing().when(awss3Uploader).delete(anyString());
		doNothing().when(postRepository).delete(post);
		//when
		postService.deletePost(postId, userId);
		//then
		verify(awss3Uploader).delete(post.getImagePath());
		verify(postRepository).delete(post);
	}

	@Test
	void deletePostByAdmin() {
		//given
		Long postId = 1L;
		Post post = mock(Post.class);
		String getImagePath = "getImagePath";

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(post.getImagePath()).thenReturn(getImagePath);
		doNothing().when(awss3Uploader).delete(anyString());
		doNothing().when(postRepository).delete(post);
		//when
		postService.deletePostByAdmin(postId);
		//then
		verify(awss3Uploader).delete(post.getImagePath());
		verify(postRepository).delete(post);
	}
}