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
@ConfigurationProperties(prefix = "mysql")
public class YtMysql {

	private String masterdriver;
	private String masterjdbcurl;
	private String masteruser;
	private String masterpassword;

	private String slavedriver;
	private String slavejdbcurl;
	private String slaveuser;
	private String slavepassword;

	private String version;

	private int maxActive = 20;
	private int maxIdle = 20;
	private int minIdle = 1;
	private int initialSize = 1;
	private int maxWait = 60000;
	private int timeBetweenEvictionRunsMillis = 60000;
	private int minEictableIdleTimeMillis = 300000;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean testWhileIdle = true;
	private String validationQuery;
	private boolean poolPreparedStatements = false;
	private int maxOpenPreparedStatements = 20;
}