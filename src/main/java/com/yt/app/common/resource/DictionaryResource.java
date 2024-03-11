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
	/**
	 * 账户类型
	 */
	public static final long SYSTEM_ADMINTYPE_1 = 1;// 超级管理员
	public static final long SYSTEM_ADMINTYPE_2 = 2;// 系统管理员
	public static final long SYSTEM_ADMINTYPE_3 = 3;// 基础管理员
	public static final long SYSTEM_ADMINTYPE_4 = 4;// 商户
	public static final long SYSTEM_ADMINTYPE_5 = 5; // 代理
	public static final long SYSTEM_ADMINTYPE_6 = 6; // 渠道

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
	public static final Integer ORDERTYPE_20 = 20; // 充值
	public static final Integer ORDERTYPE_21 = 21; // 支出
	public static final Integer ORDERTYPE_22 = 22; // 换汇

	/**
	 * 明细类型
	 */
	public static final Integer RECORDTYPE_30 = 30; // 收入待确认
	public static final Integer RECORDTYPE_31 = 31; // 支出待确认

	public static final Integer RECORDTYPE_32 = 32; // 收入通过
	public static final Integer RECORDTYPE_33 = 33; // 支出通过

	public static final Integer RECORDTYPE_34 = 34; // 收入拒絕
	public static final Integer RECORDTYPE_35 = 35; // 支出拒絕

	public static final Integer RECORDTYPE_36 = 36; // 收入取消
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
	public static final Integer PAYOUTSTATUS_51 = 51; // 代付ing
	public static final Integer PAYOUTSTATUS_52 = 52; // 成功
	public static final Integer PAYOUTSTATUS_53 = 53; // 失败或异常
	public static final Integer PAYOUTSTATUS_54 = 54; // 审核失败
	public static final Integer PAYOUTSTATUS_55 = 55; // 正在审核

	/**
	 * 回调状态
	 */

	public static final Integer PAYOUTNOTIFYSTATUS_60 = 60; // 商户发起 不需要通知
	public static final Integer PAYOUTNOTIFYSTATUS_61 = 61; // 盘口发起
	public static final Integer PAYOUTNOTIFYSTATUS_62 = 62; // 待通知
	public static final Integer PAYOUTNOTIFYSTATUS_63 = 63; // 已通知
	public static final Integer PAYOUTNOTIFYSTATUS_64 = 64; // 通知三次后失败
	public static final Integer PAYOUTNOTIFYSTATUS_65 = 65; // 通知中
	
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

}
