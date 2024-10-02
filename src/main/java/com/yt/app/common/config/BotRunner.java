package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.service.TgbotService;

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
public class BotRunner implements CommandLineRunner {

	@Autowired
	private TgbotService tgbotservice;

	@Override
	public void run(String... args) throws Exception {

		log.info("服务初始化之后，注册机器人 start...");

		// 注册机器人
		tgbotservice.initBot();

		log.info("服务初始化之后，注册机器人 end...");
	}

}
