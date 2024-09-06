package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.service.TgbotService;
import com.yt.app.common.runnable.TronGetAddressThread;
import com.yt.app.common.runnable.TronGetAddressThread2;

/**
 * <p>
 * 服务初始化之后，执行方法
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/5/22 19:29
 */
@Slf4j
@Profile("slave")
@Component
public class StartBotRunner implements CommandLineRunner {

	@Autowired
	private TgbotService tgbotservice;

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;
	
	@Override
	public void run(String... args) throws Exception {

		log.info("服务初始化之后，执行方法 start...");

		// 注册机器人
		tgbotservice.initBot();
		
		// 生成地址
		TronGetAddressThread tga = new TronGetAddressThread();
		threadpooltaskexecutor.execute(tga);

		TronGetAddressThread2 tga2 = new TronGetAddressThread2();
		threadpooltaskexecutor.execute(tga2);

		log.info("服务初始化之后，执行方法 end...");
	}

}
