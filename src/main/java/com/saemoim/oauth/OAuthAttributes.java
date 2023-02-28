package com.saemoim.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	private final Map<String, Object> attributes;
	private final String nameAttributeKey;
	private final String username;
	private final String email;
	private final String image;
	private final String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String username, String email,
		String image) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.username = username;
		this.email = email;
		this.image = image;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		if (registrationId.equals("kakao")) {
			return ofKakao(userNameAttributeName, attributes);
		}
		return null;
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		var account = (Map<String, Object>)attributes.get("kakao_account");
		var profile = (Map<String, Object>)account.get("profile");
		return OAuthAttributes.builder()
			.username(profile.get("nickname").toString())
			.email(account.get("email").toString())
			.image(profile.get("profile_image_url").toString())
			.nameAttributeKey(userNameAttributeName).build();
	}

	public User toEntity() {
		return new User(email, password, username, UserRoleEnum.USER);
	}
}
