package com.saemoim.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.security.CustomAuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {
	private final JwtUtil jwtUtil;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomOAuth2UserService oAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

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

		http.headers().frameOptions().sameOrigin(); // for SockJS WebSocket (chatting)

		http.authorizeHttpRequests()
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.requestMatchers("/docs/*").permitAll() // restDocs
			.requestMatchers("/sign-up/**").permitAll()
			.requestMatchers("/sign-in").permitAll()
			.requestMatchers("/reissue").permitAll()
			.requestMatchers("/admin/sign-in").permitAll()
			.requestMatchers("/email/**").permitAll()
			.requestMatchers("/category").permitAll()
			.requestMatchers("/tag").permitAll()
			.requestMatchers(HttpMethod.GET, "/group/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/groups/**").permitAll()
			.requestMatchers("/stomp/**").permitAll() // for SockJS WebSocket (chatting)
			.requestMatchers("/admin").hasAnyRole(UserRoleEnum.ROOT.toString())
			.requestMatchers("/admins/**").hasAnyRole(UserRoleEnum.ROOT.toString())
			.requestMatchers("/admin/**").hasAnyRole(UserRoleEnum.ADMIN.toString(), UserRoleEnum.ROOT.toString())
			.anyRequest().authenticated()
			.and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		http.cors();
		http.formLogin().disable();
		http.oauth2Login()
			.successHandler(oAuth2AuthenticationSuccessHandler)
			.userInfoEndpoint().userService(oAuth2UserService);

		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

		return http.build();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS", "PATCH")
			.exposedHeaders("Authorization", "RefreshToken");
	}
}