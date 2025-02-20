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
public class DevRunner implements CommandLineRunner {
	
	
	TronGetAddressThread5 tt1 = new TronGetAddressThread5();
	TronGetAddressThread5 tt2 = new TronGetAddressThread5();
	TronGetAddressThread5 tt3 = new TronGetAddressThread5();
	TronGetAddressThread5 tt4 = new TronGetAddressThread5();
	TronGetAddressThread5 tt5 = new TronGetAddressThread5();
	TronGetAddressThread5 tt6 = new TronGetAddressThread5();
	TronGetAddressThread5 tt7 = new TronGetAddressThread5();
	
	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Override
	public void run(String... args) throws Exception {

		log.info("slave start...");
		threadpooltaskexecutor.execute(tt1);
		threadpooltaskexecutor.execute(tt2);
		threadpooltaskexecutor.execute(tt3);
		threadpooltaskexecutor.execute(tt4);
		threadpooltaskexecutor.execute(tt5);
		threadpooltaskexecutor.execute(tt6);
		threadpooltaskexecutor.execute(tt7);
		log.info("slave end...");
	}
}
