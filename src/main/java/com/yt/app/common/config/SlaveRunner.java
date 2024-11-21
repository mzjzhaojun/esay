package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
@Component
public class SlaveRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		log.info("slave start...");

		log.info("slave end...");
	}
}
