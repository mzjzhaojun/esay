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

	/**
	 * 账户类型
	 */
	public static final long SYSTEM_ADMINTYPE_1 = 1;// 超级管理员
	public static final long SYSTEM_ADMINTYPE_2 = 2;// 系统管理员
	public static final long SYSTEM_ADMINTYPE_3 = 3;// 基础管理员
	public static final long SYSTEM_ADMINTYPE_4 = 4;// 商户
	public static final long SYSTEM_ADMINTYPE_5 = 5; // 代理
	public static final long SYSTEM_ADMINTYPE_6 = 6; // 渠道
	public static final long SYSTEM_ADMINTYPE_7 = 7; // 代收商户

	/**
	 * 支付
	 */
	public static final Integer MERCHANTORDERSTATUS_10 = 10; // 系统确认
	public static final Integer MERCHANTORDERSTATUS_11 = 11; // 通过
	public static final Integer MERCHANTORDERSTATUS_12 = 12; // 拒绝/失败
	public static final Integer MERCHANTORDERSTATUS_13 = 13; // 取消

	/**
	 * 订单类型类型
	 */
	public static final Integer ORDERTYPE_20 = 20; // 代付充值
	public static final Integer ORDERTYPE_21 = 21; // 代付提现
	public static final Integer ORDERTYPE_22 = 22; // 换汇
	public static final Integer ORDERTYPE_23 = 23; // 代付
	public static final Integer ORDERTYPE_24 = 24; // 换汇充值
	public static final Integer ORDERTYPE_25 = 25; // 换汇提现
	public static final Integer ORDERTYPE_26 = 26; // 代理提现
	public static final Integer ORDERTYPE_27 = 27; // 代收

	/**
	 * 代付资金明细类型
	 */
	public static final Integer RECORDTYPE_30 = 30; // 充值待确认
	public static final Integer RECORDTYPE_31 = 31; // 充值成功

	public static final Integer RECORDTYPE_32 = 32; // 充值失败
	public static final Integer RECORDTYPE_33 = 33; // 充值取消

	public static final Integer RECORDTYPE_34 = 34; // 代付待确认
	public static final Integer RECORDTYPE_35 = 35; // 代付成功

	public static final Integer RECORDTYPE_36 = 36; // 代付失败
	public static final Integer RECORDTYPE_37 = 37; // 代付取消

	/**
	 * 换汇资金明细类型
	 */
	public static final Integer RECORDTYPE_90 = 90; // 提现待确认
	public static final Integer RECORDTYPE_91 = 91; // 提现成功

	public static final Integer RECORDTYPE_92 = 92; // 提现失败
	public static final Integer RECORDTYPE_93 = 93; // 提现取消

	public static final Integer RECORDTYPE_94 = 94; // 换汇待确认
	public static final Integer RECORDTYPE_95 = 95; // 换汇成功

	public static final Integer RECORDTYPE_96 = 96; // 换汇失败
	public static final Integer RECORDTYPE_97 = 97; // 换汇取消

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
	public static final Integer PAYOUTSTATUS_51 = 51; // 代付ing
	public static final Integer PAYOUTSTATUS_52 = 52; // 成功
	public static final Integer PAYOUTSTATUS_53 = 53; // 失败或异常
	public static final Integer PAYOUTSTATUS_54 = 54; // 审核失败
	public static final Integer PAYOUTSTATUS_55 = 55; // 待处理

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
	public static final Integer AISLE_TYPE_EXCHANGE_72 = 72;// 代收

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
	 * 换汇资金明细类型
	 */
	public static final Integer LOG_TYPE_201 = 201; // web
	public static final Integer LOG_TYPE_202 = 202; // app

}
