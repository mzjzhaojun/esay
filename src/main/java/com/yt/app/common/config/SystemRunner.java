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
import com.yt.app.common.base.context.AuthRsaKeyContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.Tbot;
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
	private Tbot tbot;

	@Override
	public void run(String... args) throws Exception {

		log.info("服务初始化之后，执行方法 start...");

		//
		TenantIdContext.setTenantId(AppConstant.SYSTEM_TENANT_ID);

		// 初始化加密key
		AuthRsaKeyContext.setKey(RsaUtil.getPublicKey());

		// 数据字典
		dictservice.initCache();

		// 系统配置
		// this.iSysConfigService.initCache();

		// 刷新超级管理员权限
		roleservice.refreshSuperAdminPerm();

		// 启动机器人
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(tbot);

		log.info("服务初始化之后，执行方法 end...");
	}

}
