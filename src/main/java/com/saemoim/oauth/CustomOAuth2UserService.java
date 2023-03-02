package com.saemoim.oauth;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.saemoim.domain.User;
import com.saemoim.repository.UserRepository;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // Oauth2 정보 가져옴
		String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
			.getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키가 되는 필드 값(PK); 카카오는 지원 안함. 다른

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());
		Optional<User> userOptional = userRepository.findByEmail(attributes.getEmail());

		User user;
		if (userOptional.isPresent()) {
			user = userOptional.get().updateKakaoId(attributes.getAttributes().get(userNameAttributeName).toString());
		} else {
			user = attributes.toEntity();
			userRepository.save(user);
		}
		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getRole(), attributes.getAttributes());
	}
}
