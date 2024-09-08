package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.service.TgbotService;
import com.yt.app.common.runnable.TronGetAddressThread6;
import com.yt.app.common.runnable.TronGetAddressThread7;
import com.yt.app.common.runnable.TronGetAddressThread5;
import com.yt.app.common.runnable.TronGetAddressThread4;

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
public class SlaveRunner implements CommandLineRunner {

	@Autowired
	private TgbotService tgbotservice;

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Override
	public void run(String... args) throws Exception {

		log.info("服务初始化之后，执行方法 start...");

		// 注册机器人
		tgbotservice.initBot();

		// 生成地址7
		TronGetAddressThread7 tga7 = new TronGetAddressThread7();
		threadpooltaskexecutor.execute(tga7);
		// 生成地址6
		TronGetAddressThread6 tga6 = new TronGetAddressThread6();
		threadpooltaskexecutor.execute(tga6);
		// 生成地址7
		TronGetAddressThread7 tga71 = new TronGetAddressThread7();
		threadpooltaskexecutor.execute(tga71);
		// 生成地址6
		TronGetAddressThread6 tga61 = new TronGetAddressThread6();
		threadpooltaskexecutor.execute(tga61);
		// 生成地址5
		TronGetAddressThread5 tga5 = new TronGetAddressThread5();
		threadpooltaskexecutor.execute(tga5);
		// 生成地址4
		TronGetAddressThread4 tga4 = new TronGetAddressThread4();
		threadpooltaskexecutor.execute(tga4);
		// 生成地址5
		TronGetAddressThread5 tga51 = new TronGetAddressThread5();
		threadpooltaskexecutor.execute(tga51);
		// 生成地址4
		TronGetAddressThread4 tga41 = new TronGetAddressThread4();
		threadpooltaskexecutor.execute(tga41);
		// 生成地址5
		TronGetAddressThread5 tga52 = new TronGetAddressThread5();
		threadpooltaskexecutor.execute(tga52);
		// 生成地址4
		TronGetAddressThread4 tga42 = new TronGetAddressThread4();
		threadpooltaskexecutor.execute(tga42);
		log.info("服务初始化之后，执行方法 end...");
	}

}
