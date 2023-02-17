package com.saemoim.fileUpload;

import com.saemoim.domain.TimeStamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile extends TimeStamped {

	// DB 저장할 내용
	// UploadFile 의 유일 식별자 pk
	// 원래 이름
	// 저장될 이름
	// 파일 저장 경로
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String originalFilename;

	@Column(nullable = false)
	private String storeFilename;

	@Column(nullable = false)
	private String filePath;

}
