
package com.yt.app;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * by jz
 * 
 * @author dell
 *
 */ 
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AppRun extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppRun.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(new Class[] { AppRun.class }, args);
//		YtAutoCode.u().p(Arrays.asList("tgfootballgroup"));
	}
}
