package com.yt.app.common.config;

import lombok.extern.slf4j.Slf4j;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.service.DictService;
import com.yt.app.api.v1.service.RoleService;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.context.TenantIdContext;
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

	@Override
	public void run(String... args) throws Exception {

		log.info("system start...");
		
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		
		// 设置默认租户
		TenantIdContext.setTenantId(AppConstant.SYSTEM_TENANT_ID);

		// 生成密钥
		RsaUtil.InitKeys();

		// 加载字典
		dictservice.initCache();

		// 刷新初始权限
		roleservice.refreshSuperAdminPerm();

		log.info("system end...");
	}

}
