package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.common.runnable.TronGetAddressThread5;

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
@Profile("dev")
@Component
public class SlaveRunner implements CommandLineRunner {

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Override
	public void run(String... args) throws Exception {
		log.info("slave start...");
		TronGetAddressThread5 t1 = new TronGetAddressThread5();
		threadpooltaskexecutor.execute(t1);
		log.info("slave end...");
	}
}
