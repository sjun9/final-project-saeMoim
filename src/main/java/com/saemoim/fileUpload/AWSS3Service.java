package com.saemoim.fileUpload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.saemoim.domain.User;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSS3Service {

	private final AmazonS3Client amazonS3Client;
	private final UserRepository userRepository;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// MultipartFile 가져와 변환하기. S3에는 MultipartFile 업로드 안 되기 때문
	public String upload(MultipartFile multipartFile, String dirName) throws IOException {
		File uploadFile = convert(multipartFile).orElseThrow(
			() -> new IllegalArgumentException("MultipartFile -> 파일로 전환 실패"));
		return upload(uploadFile, dirName);
	}
	@Transactional
	public String upload(MultipartFile multipartFile, String dirName, Long userId) throws IOException {
		System.out.println(multipartFile.getContentType());
		if (multipartFile.isEmpty()) {
			throw new IllegalArgumentException(ErrorCode.EMPTY_FILE.getMessage());
		}
		if (multipartFile.getContentType() == null || !multipartFile.getContentType().startsWith("image")) {
			throw new IllegalArgumentException(ErrorCode.NOT_IMAGE_FILE.getMessage());
		}

		File uploadFile = convert(multipartFile).orElseThrow(
			() -> new IllegalArgumentException("MultipartFile -> 파일로 전환 실패"));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
		String imageUrl = upload(uploadFile, dirName);

		user.updateProfileImage(imageUrl);
		return imageUrl;
	}

	// S3 버킷에 파일 업로드
	private String upload(File uploadFile, String dirName) {
		String fileName = dirName + "/" + addUUID(uploadFile.getName());
		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}

	private String addUUID(String fileName) {
		return UUID.randomUUID().toString() + fileName;
	}

	// 로컬에 생성된 파일 삭제
	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일이 삭제 됐습니다.");
		} else {
			log.info("파일이 삭제 실패했습니다..");
		}
	}

	// 전환된 FIle s3에 public 읽기 원한으로 put 하기. 외부에서 정적파일을 읽을 수 있게 하기 위함.
	private String putS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, fileName, uploadFile)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	// 파일 타입 전환 (MultipartFile -> File)
	private Optional<File> convert(MultipartFile multipartFile) throws IOException {
		File convertFile = new File(multipartFile.getOriginalFilename());
		if (convertFile.createNewFile()) {
			try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
				fileOutputStream.write(multipartFile.getBytes());
			}
			return Optional.of(convertFile);
		}
		return Optional.empty();
	}


}
