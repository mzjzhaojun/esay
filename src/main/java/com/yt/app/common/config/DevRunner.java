package com.yt.app.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.common.runnable.TronGetAddressThread;

@Profile("dev")
@Component
public class DevRunner implements CommandLineRunner {

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Override
	public void run(String... args) throws Exception {
		TronGetAddressThread t1 = new TronGetAddressThread();
		threadpooltaskexecutor.execute(t1);
//		TronGetAddressThread t2 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t2);
//		TronGetAddressThread t3 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t3);
//		TronGetAddressThread t4 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t4);
//		TronGetAddressThread t5 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t5);
//		TronGetAddressThread t6 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t6);
//		TronGetAddressThread t7 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t7);
//		TronGetAddressThread t8 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t8);
	}
}
