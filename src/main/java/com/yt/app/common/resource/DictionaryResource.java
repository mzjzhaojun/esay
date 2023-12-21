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
	 * 详情类型
	 */
	public static final Integer ORDERTYPE_20 = 20; //
	public static final Integer ORDERTYPE_21 = 21; //
	public static final Integer ORDERTYPE_22 = 22; //

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
	public static final Integer PAYOUTSTATUS_50 = 50; // 待处理
	public static final Integer PAYOUTSTATUS_51 = 51; // 代付钟
	public static final Integer PAYOUTSTATUS_52 = 52; // 成功
	public static final Integer PAYOUTSTATUS_53 = 53; // 失败或异常

}
