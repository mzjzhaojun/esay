package com.yt.app.common.common;

import java.time.Duration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yt.app.common.config.YtRedis;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */

@Configuration
@EnableCaching
public class YtCachingConfigurer extends CachingConfigurerSupport {

	@Autowired
	YtRedis c;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setPort(c.getPort());
		redisStandaloneConfiguration.setHostName(c.getHost());
		redisStandaloneConfiguration.setPassword(c.getPassword());
		redisStandaloneConfiguration.setDatabase(c.getDatabase());
		JedisClientConfiguration.JedisClientConfigurationBuilder configurationBuilder = JedisClientConfiguration
				.builder();
		JedisClientConfiguration jedisClientConfiguration = configurationBuilder.usePooling().poolConfig(m()).and()
				.readTimeout(Duration.ofMillis(c.getExpire())).build();
		return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
	}

	public JedisPoolConfig m() {
		JedisPoolConfig localJedisPoolConfig = new JedisPoolConfig();
		localJedisPoolConfig.setMaxTotal(1000);
		localJedisPoolConfig.setMaxIdle(500);
		localJedisPoolConfig.setMinIdle(100);
		localJedisPoolConfig.setMaxWaitMillis(1000L);
		localJedisPoolConfig.setLifo(true);
		localJedisPoolConfig.setJmxEnabled(true);
		localJedisPoolConfig.setBlockWhenExhausted(false);
		localJedisPoolConfig.setTestOnBorrow(false);
		localJedisPoolConfig.setTestOnReturn(false);
		return localJedisPoolConfig;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisconnectionfactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		localObjectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

		template.setConnectionFactory(redisconnectionfactory);

		RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();

		Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

		redisSerializer.setObjectMapper(localObjectMapper);

		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(redisSerializer);

		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(redisSerializer);

		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + c.getHost() + ":" + c.getPort()).setTimeout(3000)
				.setDatabase(1).setPassword(c.getPassword()).setPingConnectionInterval(60000).setConnectionPoolSize(64)
				.setConnectionMinimumIdleSize(10);
		return Redisson.create(config);
	}

}