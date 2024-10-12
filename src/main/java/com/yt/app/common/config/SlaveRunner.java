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

		// 生成地址6
		TronGetAddressThread6 tga62 = new TronGetAddressThread6();
		threadpooltaskexecutor.execute(tga62);

//		// 生成地址7
//		TronGetAddressThread7 tga71 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga71);
//
//		// 生成地址6
//		TronGetAddressThread6 tga63 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga63);
//
//		// 生成地址7
//		TronGetAddressThread7 tga73 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga73);
//
//		// 生成地址6
//		TronGetAddressThread6 tga64 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga64);
//
//		// 生成地址7
//		TronGetAddressThread7 tga75 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga75);
//
//		// 生成地址6
//		TronGetAddressThread6 tga66 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga66);
//
//		// 生成地址7
//		TronGetAddressThread7 tga77 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga77);
//
//		// 生成地址6
//		TronGetAddressThread6 tga68 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga68);
//
//		// 生成地址7
//		TronGetAddressThread7 tga9 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga9);
//
//		// 生成地址6
//		TronGetAddressThread6 tga610 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga610);
//
//		// 生成地址7
//		TronGetAddressThread7 tga711 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga711);
//
//		// 生成地址6
//		TronGetAddressThread6 tga612 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga612);
//
//		// 生成地址7
//		TronGetAddressThread7 tga713 = new TronGetAddressThread7();
//		threadpooltaskexecutor.execute(tga713);
//
//		// 生成地址6
//		TronGetAddressThread6 tga614 = new TronGetAddressThread6();
//		threadpooltaskexecutor.execute(tga614);

	}
}
