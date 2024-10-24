package com.yt.app.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = "config")
public class YtConfig {

	private String workerId;

	private String workerKey;

	private String filePath;

	private String fileurl;

	private String viewurl;

	private String origin;

	private List<String> origins;

	private Integer timeout;

	private boolean build;

	private boolean cache;
	
	private String errorurl;

	public void setOrigin(String origin) {
		this.origins = Arrays.asList(origin.split(","));
		this.origin = origin;
	}
}