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
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {

	private final JwtUtil jwtUtil;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

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
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeHttpRequests()
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/log-out").permitAll()
				.requestMatchers("/allPost").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/post/**").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/posts/**").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/profile/**").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/comments/**").permitAll() // 지워도 됩니다 테스트용
				.requestMatchers("/withdraw").permitAll()
			.requestMatchers("/sign-up/**").permitAll()
			.requestMatchers("/sign-in").permitAll()
			.requestMatchers("/reissue").permitAll()
			.requestMatchers("/admin/sign-in").permitAll()
			.requestMatchers("/email/**").permitAll()
			.requestMatchers("/category").permitAll()
			.requestMatchers("/tag").permitAll()
			.requestMatchers(HttpMethod.GET, "/group/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/groups/**").permitAll()
			.requestMatchers("/admin").hasAnyRole(UserRoleEnum.ROOT.toString())
			.requestMatchers("/admins/**").hasAnyRole(UserRoleEnum.ROOT.toString())
			.requestMatchers("/admin/**").hasAnyRole(UserRoleEnum.ADMIN.toString(), UserRoleEnum.ROOT.toString())
			.anyRequest().authenticated() // 되는데요 안됩니다 내말뤼 내말뤼
			.and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		http.cors();
		http.formLogin().disable();

		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

		return http.build();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS", "PATCH")
			.exposedHeaders("Authorization", "Refresh_Token");
		// .allowedOrigins("*");
	}
}