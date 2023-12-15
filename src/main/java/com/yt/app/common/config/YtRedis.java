package com.yt.app.common.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "redis")
public class YtRedis {

	private String host;

	private int port;

	private String password;

	private int timeout;

	private int expire;
}