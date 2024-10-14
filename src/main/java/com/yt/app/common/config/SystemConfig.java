package com.yt.app.common.config;

import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.DispatcherServlet;

import com.yt.app.common.config.YtConfig;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@Configuration
@EnableAsync
public class SystemConfig {

	@Autowired
	YtConfig g;

	@Bean(name = "Snowflake")
	public Snowflake idworker() {
		Snowflake snowflake = IdUtil.getSnowflake(Long.parseLong(this.g.getWorkerId()), Long.parseLong(this.g.getWorkerKey()));
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

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		int i = Runtime.getRuntime().availableProcessors();
		executor.setCorePoolSize(i * 4);
		executor.setMaxPoolSize(i * 4);
		executor.setQueueCapacity(i * 4 * 10);
		executor.setThreadNamePrefix("ThreadPoolTaskExecutor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setKeepAliveSeconds(60);
		executor.initialize();
		return executor;
	}
}
