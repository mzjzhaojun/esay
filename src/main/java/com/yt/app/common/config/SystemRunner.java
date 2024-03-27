package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.yt.app.api.v1.service.DictService;
import com.yt.app.api.v1.service.RoleService;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.Channelbot;
import com.yt.app.common.bot.Merchantbot;
import com.yt.app.common.bot.Messagebot;
import com.yt.app.common.util.RsaUtil;

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
public class SystemRunner implements CommandLineRunner {
	@Autowired
	private RoleService roleservice;

	@Autowired
	private DictService dictservice;

	@Autowired
	private Merchantbot merchantbot;

	@Autowired
	private Channelbot channelbot;
	
	@Autowired
	private Messagebot messagebot;

	@Override
	public void run(String... args) throws Exception {

		log.info("服务初始化之后，执行方法 start...");

		// 设置默认租户
		TenantIdContext.setTenantId(AppConstant.SYSTEM_TENANT_ID);

		// 生成密钥
		RsaUtil.InitKeys();

		// 加载字典
		dictservice.initCache();

		// 刷新初始权限
		roleservice.refreshSuperAdminPerm();

		// 注册机器人
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(merchantbot);
			botsApi.registerBot(channelbot);
			botsApi.registerBot(messagebot);
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}

		log.info("服务初始化之后，执行方法 end...");
	}

}
