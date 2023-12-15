package com.yt.app;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yt.app.common.common.Ytk;

/**
 * by jz
 * 
 * @author dell
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableHypermediaSupport(type = { HypermediaType.HAL })
@EnableAspectJAutoProxy(proxyTargetClass = false)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 30)
public class Run extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Run.class);
	}

	public static void main(String[] args) throws Exception {

		// "agentaccount", "agentaccountapply", "agentaccountapplyjourna",
		// "agentaccountorder","channelaccount", "channelaccountapply",
		// "channelaccountapplyjourna", "channelaccountorder","merchantaccount",
		// "merchantaccountapply", "merchantaccountapplyjournal",
		// "merchantaccountorder","systemaccount", "systemcapitalrecord"

		Ytk.u().p(Arrays.asList("tgconfig"));
	}
}