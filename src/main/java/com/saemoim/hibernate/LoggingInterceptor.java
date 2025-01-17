// package com.saemoim.hibernate;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerInterceptor;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Component
// public class LoggingInterceptor implements HandlerInterceptor {
//
// 	private static final String QUERY_COUNT_LOG_FORMAT = "STATUS_CODE: {}, METHOD: {}, URL: {}, QUERY_COUNT: {}";
//
// 	private final ApiQueryCounter apiQueryCounter;
//
// 	public LoggingInterceptor(final ApiQueryCounter apiQueryCounter) {
// 		this.apiQueryCounter = apiQueryCounter;
// 	}
//
// 	@Override
// 	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
// 		final Object handler, final Exception ex) {
// 		final int queryCount = apiQueryCounter.getCount();
//
// 		log.info(QUERY_COUNT_LOG_FORMAT, response.getStatus(), request.getMethod(), request.getRequestURI(),
// 			queryCount);
// 	}
// }