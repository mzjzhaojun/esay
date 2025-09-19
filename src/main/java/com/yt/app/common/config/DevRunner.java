package com.yt.app.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.yt.app.common.bot.TestBot;
import com.yt.app.common.runnable.TronGetAddressThread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
public class DevRunner implements CommandLineRunner {

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	private TelegramBotsApi botsApi;

	@Autowired
	private TestBot testbot;

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
//		TronGetAddressThread t9 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t9);
//		TronGetAddressThread t10 = new TronGetAddressThread();
//		threadpooltaskexecutor.execute(t10);

		botsApi = new TelegramBotsApi(DefaultBotSession.class);

		log.info("bot test start...");
		// 注册机器人
		botsApi.registerBot(testbot);
		log.info("bot test end...");
	}
}
