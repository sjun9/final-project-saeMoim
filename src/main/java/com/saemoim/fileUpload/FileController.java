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

	private final AWSS3Uploader awss3Uploader;
	// s3에 파일 업로드 해보기
	@PostMapping("/upload")
	public String upload(@RequestParam("img") MultipartFile multipartFile ) throws IOException {
		return awss3Uploader.upload(multipartFile, "static");
	}



	@PostMapping("/post/image")
	public String uploadPostImage(@RequestParam("img") MultipartFile multipartFile) throws IOException {
		return awss3Uploader.upload(multipartFile, "post");
	}
}
