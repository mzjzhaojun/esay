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
		MybatisPlusConfig.TENANT_ID_TABLE.add("channelaccountrecord");
		MybatisPlusConfig.TENANT_ID_TABLE.add("channelaccountorder");

		MybatisPlusConfig.TENANT_ID_TABLE.add("merchant");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaisle");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantqrcodeaisle");

		MybatisPlusConfig.TENANT_ID_TABLE.add("payout");
		MybatisPlusConfig.TENANT_ID_TABLE.add("payoutmerchantaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("payoutmerchantaccountrecord");
		MybatisPlusConfig.TENANT_ID_TABLE.add("payoutmerchantaccountorder");

		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantaccountbank");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantcustomerbanks");

		MybatisPlusConfig.TENANT_ID_TABLE.add("agent");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccountbank");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccountrecord");
		MybatisPlusConfig.TENANT_ID_TABLE.add("agentaccountorder");

		MybatisPlusConfig.TENANT_ID_TABLE.add("systemaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("systemaccountrecord");

		MybatisPlusConfig.TENANT_ID_TABLE.add("exchange");
		MybatisPlusConfig.TENANT_ID_TABLE.add("exchangemerchantaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("exchangemerchantaccountrecord");
		MybatisPlusConfig.TENANT_ID_TABLE.add("exchangemerchantaccountorder");

		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcode");
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodeaisle");
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodeaisleqrcode");
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodeaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodeaccountorder");
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodeaccountrecord");

		MybatisPlusConfig.TENANT_ID_TABLE.add("income");
		MybatisPlusConfig.TENANT_ID_TABLE.add("incomemerchantaccount");
		MybatisPlusConfig.TENANT_ID_TABLE.add("incomemerchantaccountrecord");
		MybatisPlusConfig.TENANT_ID_TABLE.add("incomemerchantaccountorder");

		// 报表
		MybatisPlusConfig.TENANT_ID_TABLE.add("channelstatisticalreports");
		MybatisPlusConfig.TENANT_ID_TABLE.add("merchantstatisticalreports");
		MybatisPlusConfig.TENANT_ID_TABLE.add("systemstatisticalreports");
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodestatisticalreports");

		// tgrame
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgbotgroup");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgbotgrouprecord");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgchannelgroup");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgchannelgrouplabel");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgmerchantchannelmsg");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgmerchantgroup");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgmerchantgrouplabel");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tgmessagegroup");
		// tron
		MybatisPlusConfig.TENANT_ID_TABLE.add("tron");
		MybatisPlusConfig.TENANT_ID_TABLE.add("tronaddress");

		// 黑名单
		MybatisPlusConfig.TENANT_ID_TABLE.add("blocklist");

		// 支付宝转账记录
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodetransferrecord");
		// 会员绑卡信息
		MybatisPlusConfig.TENANT_ID_TABLE.add("qrcodpaymember");
	}

}
