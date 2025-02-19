package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.yt.app.common.bot.NDChannelBot;
import com.yt.app.common.bot.NDMerchantBot;

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
public class NDBotRunner implements CommandLineRunner {

	private TelegramBotsApi botsApi;

	@Autowired
	private NDMerchantBot merchantbot;

	@Autowired
	private NDChannelBot channelbot;

	@Override
	public void run(String... args) throws Exception {

		botsApi = new TelegramBotsApi(DefaultBotSession.class);

		log.info("ndbot start...");
		// 注册机器人
		botsApi.registerBot(merchantbot);
		botsApi.registerBot(channelbot);
		log.info("ndbot end...");
	}

}
