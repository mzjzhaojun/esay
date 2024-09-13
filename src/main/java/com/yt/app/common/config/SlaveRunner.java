package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.common.runnable.TronGetAddressThread6;
import com.yt.app.common.runnable.TronGetAddressThread7;

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
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Override
	public void run(String... args) throws Exception {

		log.info("slave 服务初始化之后，执行生成靓号地址...");

		// 生成地址7
		TronGetAddressThread7 tga7 = new TronGetAddressThread7();
		threadpooltaskexecutor.execute(tga7);

		// 生成地址7
		TronGetAddressThread7 tga71 = new TronGetAddressThread7();
		threadpooltaskexecutor.execute(tga71);

		// 生成地址7
		TronGetAddressThread7 tga72 = new TronGetAddressThread7();
		threadpooltaskexecutor.execute(tga72);

		// 生成地址7
		TronGetAddressThread7 tga73 = new TronGetAddressThread7();
		threadpooltaskexecutor.execute(tga73);

		// 生成地址6
		TronGetAddressThread6 tga6 = new TronGetAddressThread6();
		threadpooltaskexecutor.execute(tga6);

		// 生成地址6
		TronGetAddressThread6 tga61 = new TronGetAddressThread6();
		threadpooltaskexecutor.execute(tga61);

		// 生成地址6
		TronGetAddressThread6 tga62 = new TronGetAddressThread6();
		threadpooltaskexecutor.execute(tga62);
	}
}
