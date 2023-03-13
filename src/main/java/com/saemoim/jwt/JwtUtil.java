package com.saemoim.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.security.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String AUTHORIZATION_NAME = "Name";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_TIME = 20 * 60 * 1000L;
	public static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L;

	private final UserDetailsServiceImpl userDetailsService;

	@Value("${jwt.secret.key}")
	private String secretKey;

	private Key key;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public Optional<String> resolveToken(String bearerToken) {
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return Optional.of(bearerToken.substring(7));    // String Index = 7접두사 빼고 반환
		}
		return Optional.empty();
	}

	public String createAccessToken(Long id, String username, UserRoleEnum role) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(id))
				.claim(AUTHORIZATION_NAME, username)
				.claim(AUTHORIZATION_KEY, String.valueOf(role))
				.setExpiration(new Date(date.getTime() + TOKEN_TIME))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	public String createRefreshToken(Long userId) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String getSubjectFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	// 인증 객체 생성
	public Authentication createAuthentication(Long id, String username, UserRoleEnum role) {
		UserDetails userDetails = userDetailsService.loadUserInfoByJwt(id, username, role);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}