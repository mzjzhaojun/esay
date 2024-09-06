package com.yt.app;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.TimeZone;

import com.yt.app.common.common.YtAutoCode;
import com.yt.app.common.util.NumberUtil;

/**
 * by jz
 * 
 * @author dell
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableHypermediaSupport(type = { HypermediaType.HAL })
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AppRun extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppRun.class);
	}

	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SpringApplication.run(new Class[] { AppRun.class }, args);
		// YtAutoCode.u().p(Arrays.asList("tron"));
	}
}