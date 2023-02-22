package com.saemoim.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.jwt.JwtAuthFilter;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.security.CustomAuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity    // @Secured 어노테이션 활성화
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {

	private final JwtUtil jwtUtil;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	// 비밀번호 단방향 암호화 인코더
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// static resources ( css, js, images 등 ) 자원에 대한 접근 허용
		return (web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// jwt 방식에선 사용하지 않아도 됨.
		http.csrf().disable();
		// Session 방식 사용하지 않고 JWT 방식 사용하기 위한 설정
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// 내용 추가 필요함. 로그인 페이지, 회원가입 페이지 등
		http.authorizeHttpRequests()
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.requestMatchers("/sign-up").permitAll()
			.requestMatchers("/sign-in").permitAll()
			.requestMatchers("/reissue").permitAll()
			.requestMatchers("/log-out").permitAll()
				.requestMatchers("/allPost").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/posts/**").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/profile/**").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/comments/**").permitAll() // 지워도 됩니다 테스트용
			.requestMatchers("/withdraw").permitAll()
			.requestMatchers("/admin/sign-in").permitAll()
			.requestMatchers("/email/**").permitAll()
			.requestMatchers("/category").permitAll()
			.requestMatchers(HttpMethod.GET, "/group/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/groups/**").permitAll()
			.requestMatchers("/admin").hasAnyRole(UserRoleEnum.ROOT.toString())
			.requestMatchers("/admins/**").hasAnyRole(UserRoleEnum.ROOT.toString())
			.requestMatchers("/admin/**").hasAnyRole(UserRoleEnum.ADMIN.toString(), UserRoleEnum.ROOT.toString())
			.and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		// .anyRequest().authenticated();	// 모든 요청에 대해 인증. 당장 사용하지 않으므로 주석 처리

		http.formLogin().disable();
		// .loginPage("/로그인form url").permitAll();

		// http.addFilterBefore(new CustomSecurityFilter(userDetailsService, passwordEncoder()),
		// 	UsernamePasswordAuthenticationFilter.class);

		// 접근 제한 페이지 이동
		// http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

		return http.build();    // 상기 설정들을 빌드하여 리턴
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS", "PATCH")
			.exposedHeaders("Authorization");
	}
}