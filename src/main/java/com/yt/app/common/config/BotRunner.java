package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.yt.app.common.bot.ChannelBot;
import com.yt.app.common.bot.MerchantBot;
import com.yt.app.common.bot.TronBot;

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
@Profile("master")
@Component
public class BotRunner implements CommandLineRunner {

	private TelegramBotsApi botsApi;

	@Autowired
	private MerchantBot merchantbot;

	@Autowired
	private ChannelBot channelbot;

	@Autowired
	private TronBot tronbot;

	@Override
	public void run(String... args) throws Exception {

		botsApi = new TelegramBotsApi(DefaultBotSession.class);

		log.info("服务初始化之后，注册机器人 start...");

		// 注册机器人
		botsApi.registerBot(merchantbot);
		botsApi.registerBot(channelbot);
		botsApi.registerBot(tronbot);

		log.info("服务初始化之后，注册机器人 end...");
	}

}
