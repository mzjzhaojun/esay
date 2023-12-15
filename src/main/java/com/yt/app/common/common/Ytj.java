package com.yt.app.common.common;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.DispatcherServlet;

import com.yt.app.common.config.YtConfig;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Configuration
public class Ytj {

	@Autowired
	YtConfig g;

	@Bean(name = "Snowflake")
	public Snowflake idworker() {
		Snowflake snowflake = IdUtil.getSnowflake(Long.parseLong(this.g.getWorkerId()),
				Long.parseLong(this.g.getWorkerKey()));
		return snowflake;
	}

	@Bean(name = "MultipartConfigElement")
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory localMultipartConfigFactory = new MultipartConfigFactory();
		localMultipartConfigFactory.setMaxFileSize(DataSize.ofMegabytes(1024));
		localMultipartConfigFactory.setMaxRequestSize(DataSize.ofMegabytes(1024));
		return localMultipartConfigFactory.createMultipartConfig();
	}

	@Bean(name = "DispatcherServlet")
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet localDispatcherServlet = new DispatcherServlet();
		localDispatcherServlet.setDispatchOptionsRequest(true);
		localDispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return localDispatcherServlet;
	}

}