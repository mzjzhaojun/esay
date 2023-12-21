package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */
@Getter
@Setter
public class Merchant extends YtBaseEntity<Merchant> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String username;
	String password;
	String nikname;
	String code;
	String remark;
	Double balance;
	Boolean status;
	Double todaycost;
	Double todaycount;
	Double count;
	Double exchange;
	Double onecost;
	Double downpoint;
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

	public Merchant() {
	}

	public Merchant(Long id, String name, String nikname, String code, String remark, Double balance, Boolean status,
			Double todaycost, Double todaycount, Double count, Double exchange, Double onecost, Double downpoint,
			Long agentid, String agentname, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, Boolean backpay, Boolean clearingtype, String ipaddress, Long tenant_id,
			Long userid, Integer version) {
		this.id = id;
		this.name = name;
		this.nikname = nikname;
		this.code = code;
		this.remark = remark;
		this.balance = balance;
		this.status = status;
		this.todaycost = todaycost;
		this.todaycount = todaycount;
		this.count = count;
		this.exchange = exchange;
		this.onecost = onecost;
		this.downpoint = downpoint;
		this.agentid = agentid;
		this.agentname = agentname;
		this.backpay = backpay;
		this.clearingtype = clearingtype;
		this.ipaddress = ipaddress;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.version = version;
	}
}