package com.yt.app.api.v1.entity;

import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchant extends YtBaseEntity<Merchant> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String username;
	String password;
	String nikname;
	String code;
	String remark;
	String type;
	Double balance;
	Double usdtbalance;
	Boolean ipstatus;
	Boolean status;
	Double todaycost;
	Double todaycount;
	Double count;
	Double todayincomecount;
	Double incomecount;
	Double exchange;
	Double collection;
	Double onecost;
	Double downpoint;
	Double exchangeonecost;
	Double exchangedownpoint;
	Double incomedownpoint;
	Long agentid;
	String agentname;
	Boolean backpay;
	Boolean clearingtype;
	String ipaddress;
	Long tenant_id;
	Long userid;
	Integer version;

	//
	String appkey;
	String apireusultip;
}