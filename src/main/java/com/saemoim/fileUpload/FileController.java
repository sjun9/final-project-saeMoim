package com.saemoim.fileUpload;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FileController {

	private final AWSS3Service awss3Service;
	// s3에 파일 업로드 해보기
	@PostMapping("/upload")
	public String upload(@RequestParam("img") MultipartFile multipartFile ) throws IOException {
		return awss3Service.upload(multipartFile, "static");
	}

	@PostMapping("/profile/image")
	public String uploadProfileImage(@RequestParam("img") MultipartFile multipartFile
		,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
		return awss3Service.upload(multipartFile, "profile",userDetails.getId());
	}

	@PostMapping("/post/image")
	public String uploadPostImage(@RequestParam("img") MultipartFile multipartFile) throws IOException {
		return awss3Service.upload(multipartFile, "post");
	}
}
