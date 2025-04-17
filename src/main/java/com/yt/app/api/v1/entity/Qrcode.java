package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Qrcode extends YtBaseEntity<Qrcode> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String name;
	String nkname;
	Long pid;
	String code;
	Boolean status;
	Double balance;
	Double collection;
	Integer expireminute;
	String type;
	Integer min;
	Integer max;
	Integer weight;
	Integer mtorder;
	Boolean dynamic;
	Boolean accountsplit;
	String smid;
	Boolean firstmatch;
	String firstmatchmoney;
	Integer yestodayorder;
	Double yestodayincome;
	Integer todayorder;
	Double todayincome;
	String appid;
	String appprivatekey;
	String apppublickey;
	String alipaypublickey;
	String alipayprovatekey;
	Double limits;
	Double todaybalance;
	Double incomesum;
	Integer ordersum;
	String apirest;
	String notifyurl;
	String payoutnotifyurl;
	String remark;
	Integer version;
}