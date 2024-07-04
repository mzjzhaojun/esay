package com.yt.app.common.config;

import org.springframework.context.annotation.Configuration;

import com.yt.app.common.mybatis.MybatisPlusConfig;

/**
 * <p>
 * MybatisPlus配置类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/23 9:46
 */
@Configuration
public class SystemMybatisPlusConfig {

	static {

		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_dept");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_post");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_role");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_role_menu");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_role_scope");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_user");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_user_role");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_config");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_file");
		MybatisPlusConfig.TENANT_ID_TABLE.add("t_sys_logs");
		//

		MybatisPlusConfig.TENANT_ID_TABLE.add("aisle");
		MybatisPlusConfig.TENANT_ID_TABLE.add("aislechannel");

		MybatisPlusConfig.TENANT_ID_TABLE.add("channel");
		MybatisPlusConfig.TENANT_ID_TABLE.add("channelaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("channelaccountapplyjourna");
		MybatisPlusConfig.TENANT_ID_TABLE.add("channelaccountorder");

		MybatisPlusConfig.TENANT_ID_TABLE.add("merchant");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaisle");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaccountbank");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaccountapplyjournal");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaccountorder");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantcustomerbanks");

		MybatisPlusConfig.TENANT_ID_TABLE.add("agent");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccountbank");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccountapplyjourna");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccountorder");

		MybatisPlusConfig.TENANT_ID_TABLE.add("payout");
		MybatisPlusConfig.TENANT_ID_TABLE.add("exchange");
		MybatisPlusConfig.TENANT_ID_TABLE.add("systemaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("systemcapitalrecord");

		// MybatisPlusConfig.TENANT_ID_TABLE.add("tgbot");
		// MybatisPlusConfig.TENANT_ID_TABLE.add("tgbotgroup");
		// MybatisPlusConfig.TENANT_ID_TABLE.add("tgbotgrouprecord");
		// MybatisPlusConfig.TENANT_ID_TABLE.add("tgmerchantlabel");
		// MybatisPlusConfig.TENANT_ID_TABLE.add("tgchannelgroup");
	}

}
