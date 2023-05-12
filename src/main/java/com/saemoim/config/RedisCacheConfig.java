package com.saemoim.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {
	@Value("${spring.data.redis.cache.host}")
	private String host;
	@Value("${spring.data.redis.cache.port}")
	private int port;

	@Bean(name = "redisCacheConnectionFactory")
	public RedisConnectionFactory redisCacheConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	public CacheManager cacheManager(
		@Qualifier("redisCacheConnectionFactory") RedisConnectionFactory connectionFactory) {
		return RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(defaultConfig())
			.withInitialCacheConfigurations(confMap())
			.build();
	}

	private RedisCacheConfiguration defaultConfig() {
		return RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
			.entryTtl(Duration.ofMinutes(1));
	}

	private Map<String, RedisCacheConfiguration> confMap() {
		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
		cacheConfigurations.put("popularGroup", defaultConfig().entryTtl(Duration.ofSeconds(30L)));
		return cacheConfigurations;
	}
}
