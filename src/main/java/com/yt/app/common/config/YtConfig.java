package com.yt.app.common.config;

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

	private String origin;

	private Integer timeout;

	private boolean build;

	private boolean cache;

	private String aligeteway;

	private String bindcard;

	private String bindcardconfirm;

	private String protocolpaypre;

	private String paymentquery;

}