package com.yt.app.common.common;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
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

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
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
public class Yth extends CachingConfigurerSupport {

	@Autowired
	YtRedis c;

	@Bean(name = "JedisConnectionFactory")
	public JedisConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setPort(this.c.getPort());
		redisStandaloneConfiguration.setHostName(this.c.getHost());
		redisStandaloneConfiguration.setPassword(this.c.getPassword());
		redisStandaloneConfiguration.setDatabase(2);
		JedisClientConfiguration.JedisClientConfigurationBuilder configurationBuilder = JedisClientConfiguration
				.builder();
		JedisClientConfiguration jedisClientConfiguration = configurationBuilder.usePooling().poolConfig(m()).build();
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

	@Bean(name = "RedisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory paramRedisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
		FastJsonRedisSerializer<Object> redisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
		template.setDefaultSerializer(redisSerializer);
		template.setEnableDefaultSerializer(true);
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(redisSerializer);
		template.setHashValueSerializer(redisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		a(template);
		template.setConnectionFactory(paramRedisConnectionFactory);
		template.afterPropertiesSet();
		return template;
	}

	private void a(RedisTemplate<String, Object> paramRedisTemplate) {
		Jackson2JsonRedisSerializer<Object> localJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		localObjectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		localJackson2JsonRedisSerializer.setObjectMapper(localObjectMapper);
		paramRedisTemplate.setValueSerializer(localJackson2JsonRedisSerializer);
	}

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + this.c.getHost() + ":" + this.c.getPort()).setTimeout(3000)
				.setPingConnectionInterval(60000).setConnectionPoolSize(64).setConnectionMinimumIdleSize(10);
		return Redisson.create(config);
	}

}