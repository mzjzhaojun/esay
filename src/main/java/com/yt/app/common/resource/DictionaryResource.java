package com.yt.app.common.resource;

import java.io.Serializable;

/**
 * 
 * 字典
 * 
 *
 */
public class DictionaryResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	public static final Integer IS_DELETE_0 = 0;
	public static final Integer IS_DELETE_1 = 1;

	public static final String SUCCESS = "10000"; // 成功
	public static final String PAYING = "10003"; // 用户支付中
	public static final String FAILED = "40004"; // 失败
	public static final String ERROR = "20000"; // 系统异常

	// 代收
	public static final String KFAISLE = "KF"; // KF
	public static final String EGAISLE = "二狗"; // 二狗
	public static final String WDAISLE = "豌豆"; // WDAISLE
	public static final String RBLAISLE = "日不落"; // WDAISLE
	public static final String GZAISLE = "公子"; // WDAISLE
	public static final String WJAISLE = "玩家"; // WJAISLE
	public static final String FCAISLE = "翡翠"; // FCAISLE
	public static final String AKLAISLE = "奥克兰"; // AKLAISLE
	public static final String FHLAISLE = "飞黄运通"; // 飞黄运通
	public static final String YSAISLE = "易生"; // 易生
	public static final String XSAISLE = "新生"; // 新生
	public static final String ZSAISLE = "张三";// 张三
	public static final String TONGYUSNAISLE = "通源";// 通源
	public static final String ONEPLUSAISLE = "oneplus";// oneplus

	// 代付
	public static final String DFSNAISLE = "十年"; //
	public static final String DFSSAISLE = "盛世"; //
	public static final String DFTXAISLE = "天下"; //
	public static final String DFYSAISLE = "易生"; //
	public static final String DFXRAISLE = "旭日"; //
	public static final String DFSXAISLE = "守信"; //
	public static final String DFLJAISLE = "灵境"; //
	public static final String DFHYTAISLE = "HYT"; //
	public static final String DFXJAISLE = "仙剑"; //
	public static final String DFQWAISLE = "青蛙"; //
	public static final String DF8GAISLE = "8G"; //
	public static final String DFHYAISLE = "环宇"; //
	/**
	 * 账户类型
	 */
	public static final long SYSTEM_ADMINTYPE_1 = 1;// 超级管理员
	public static final long SYSTEM_ADMINTYPE_2 = 2;// 系统管理员
	public static final long SYSTEM_ADMINTYPE_3 = 3;// 基础管理员
	public static final long SYSTEM_ADMINTYPE_4 = 4;// 代付商户
	public static final long SYSTEM_ADMINTYPE_5 = 5; // 代理
	public static final long SYSTEM_ADMINTYPE_6 = 6; // 渠道
	public static final long SYSTEM_ADMINTYPE_7 = 7; // 代收商户
	public static final long SYSTEM_ADMINTYPE_8 = 8; // 换汇商户

	/**
	 * 订单类型
	 */
	public static final Integer ORDERTYPE_10 = 10; // 渠道充值
	public static final Integer ORDERTYPE_16 = 16; // 自营渠道提现

	public static final Integer ORDERTYPE_11 = 11; // 商户代付充值

	public static final Integer ORDERTYPE_12 = 12; // 系统代付订单
	public static final Integer ORDERTYPE_14 = 14; // 系统代收订单
	public static final Integer ORDERTYPE_26 = 26; // 系统代理订单

	public static final Integer ORDERTYPE_21 = 21; // 代理提现
	public static final Integer ORDERTYPE_13 = 13; // 代理换汇订单
	public static final Integer ORDERTYPE_23 = 23; // 代理代付订单
	public static final Integer ORDERTYPE_20 = 20; // 代理代收订单

	public static final Integer ORDERTYPE_22 = 22; // 商户换汇订单
	public static final Integer ORDERTYPE_28 = 28; // 商户代收提现
	public static final Integer ORDERTYPE_29 = 29; // 商户代付订单
	public static final Integer ORDERTYPE_15 = 25; // 商户代付提现

	public static final Integer ORDERTYPE_24 = 24; // 换汇充值
	public static final Integer ORDERTYPE_25 = 25; // 换汇提现

	public static final Integer ORDERTYPE_27 = 27; // 代收订单
	public static final Integer ORDERTYPE_18 = 18; // 代付订单
	public static final Integer ORDERTYPE_19 = 19; // 換汇订单

	public static final Integer ORDERTYPE_17 = 17; // 渠道代付订单

	/**
	 * 收入支出资金明细类型
	 */
	public static final Integer RECORDTYPE_30 = 30; // 收入待确认
	public static final Integer RECORDTYPE_31 = 31; // 收入成功
	public static final Integer RECORDTYPE_32 = 32; // 收入失败
	public static final Integer RECORDTYPE_33 = 33; // 收入取消
	public static final Integer RECORDTYPE_34 = 34; // 支出待确认
	public static final Integer RECORDTYPE_35 = 35; // 支出成功
	public static final Integer RECORDTYPE_36 = 36; // 支出失败
	public static final Integer RECORDTYPE_37 = 37; // 支出取消

	/**
	 * 银行卡类型
	 */
	public static final Integer BANKCARDTYPE_40 = 40; // 数字币usdt
	public static final Integer BANKCARDTYPE_41 = 41; // 银联
	public static final Integer BANKCARDTYPE_42 = 42; // Visa

	/**
	 * 代付状态
	 */
	public static final Integer PAYOUTSTATUS_50 = 50; // 新增
	public static final Integer PAYOUTSTATUS_51 = 51; // 取消
	public static final Integer PAYOUTSTATUS_52 = 52; // 成功
	public static final Integer PAYOUTSTATUS_53 = 53; // 失败
	public static final Integer PAYOUTSTATUS_54 = 54; // 结算
	public static final Integer PAYOUTSTATUS_55 = 55; // 分账

	/**
	 * 回调状态
	 */

	public static final Integer PAYOUTNOTIFYSTATUS_60 = 60; // 无通知
	public static final Integer PAYOUTNOTIFYSTATUS_61 = 61; // 需通知
	public static final Integer PAYOUTNOTIFYSTATUS_62 = 62; // 待通知
	public static final Integer PAYOUTNOTIFYSTATUS_63 = 63; // 已通知
	public static final Integer PAYOUTNOTIFYSTATUS_64 = 64; // 通知失败
	public static final Integer PAYOUTNOTIFYSTATUS_65 = 65; // 正在通知

	/**
	 * 通道类型
	 */
	public static final Integer AISLE_TYPE_PAYOUT_70 = 70;// 代付
	public static final Integer AISLE_TYPE_EXCHANGE_71 = 71;// 换汇

	/**
	 * 换汇资金明细类型
	 */
	public static final Integer EXCHANGETYPE_80 = 80; // 换汇待确认
	public static final Integer EXCHANGETYPE_81 = 81; // 换汇通过
	public static final Integer EXCHANGETYPE_82 = 82; // 收入通过
	public static final Integer EXCHANGETYPE_83 = 83; // 支出通过
	public static final Integer EXCHANGETYPE_84 = 84; // 收入拒絕
	public static final Integer EXCHANGETYPE_85 = 85; // 支出拒絕
	public static final Integer EXCHANGETYPE_86 = 86; // 收入取消
	public static final Integer EXCHANGETYPE_87 = 87; // 支出取消

	public static final Integer TGBOT_TYPE_M = 101; // 商户
	public static final Integer TGBOT_TYPE_C = 102; // 运营
	public static final Integer TGBOT_TYPE_MSG = 103; // 记账

	public static final Integer TGBOTGROUPRECORD_TYPE_INCOME = 111; // 收入
	public static final Integer TGBOTGROUPRECORD_TYPE_WITHDRAW = 112; // 减款
	public static final Integer TGBOTGROUPRECORD_TYPE_USDT = 113; // 下发

	public static final Integer BANK_TYPE_121 = 121; // 银行卡
	public static final Integer BANK_TYPE_122 = 122; // 微信
	public static final Integer BANK_TYPE_123 = 123; // 支付宝

	/**
	 * 登錄日志類型
	 */
	public static final Integer LOG_TYPE_201 = 201; // web
	public static final Integer LOG_TYPE_202 = 202; // app

	// 项目类型
	// 自营产品编码
	public static final String PRODUCT_YPLWAP = "YPLWAP";
	public static final Integer PROJECT_TYPE_501 = 501;// 易票联
	public static final String PRODUCT_ZFTWAP = "ZFTWAP";
	public static final Integer PROJECT_TYPE_502 = 502;// 支付宝直付通手机网站支付
	public static final String PRODUCT_HUIFUTXWAP = "HUIFUTX";
	public static final Integer PROJECT_TYPE_503 = 503;

	public static final Integer PROJECT_TYPE_504 = 504;
	public static final Integer PROJECT_TYPE_505 = 505;
	public static final Integer PROJECT_TYPE_506 = 506;
	public static final Integer PROJECT_TYPE_507 = 507;
	public static final Integer PROJECT_TYPE_508 = 508;
	public static final Integer PROJECT_TYPE_509 = 509;
	public static final Integer PROJECT_TYPE_510 = 510;
	public static final Integer PROJECT_TYPE_511 = 511;

	// 兑换类型
	public static final Integer EXCHANGE_TYPE_601 = 601;// trx兑换
	public static final Integer EXCHANGE_TYPE_602 = 602;// 充值

	// 支付宝状态
	public static final Integer ALIPAY_STATUS_701 = 701;// 支付成功
	public static final Integer ALIPAY_STATUS_702 = 702;// 分账成功
	public static final Integer ALIPAY_STATUS_703 = 703;// 下载成功

}
