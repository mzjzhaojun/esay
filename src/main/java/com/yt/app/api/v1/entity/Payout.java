package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */
@Getter
@Setter
public class Payout extends YtBaseEntity<Payout> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Double merchantcost;
	Double merchantdeal;
	Double merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Double agentincome;
	Long channelid;
	String channelname;
	String channelordernum;
	Double channelcost;
	Double channeldeal;
	Double channelpay;
	String accname;
	String accnumer;
	String bankcode;
	String bankaddress;
	Double amount;
	Integer status;
	String statusname;
	java.util.Date successtime;
	Long backlong;
	String imgurl;
	Double income;
	String notifyurl;
	String remark;
	Integer version;

	public Payout() {
	}

	public Payout(Long id, Long tenant_id, Long userid, String ordernum, Long merchantid, String merchantname,
			String merchantcode, String merchantordernum, Double merchantcost, Double merchantdeal, Double merchantpay,
			Long aisleid, String aislename, Long agentid, String agentordernum, Double agentincome, Long channelid,
			String channelname, String channelordernum, Double channelcost, Double channeldeal, Double channelpay,
			String accname, String accnumer, String bankaddress, Double amount, Integer status,
			java.util.Date successtime, Long backlong, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String imgurl, Double income, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.ordernum = ordernum;
		this.merchantid = merchantid;
		this.merchantname = merchantname;
		this.merchantcode = merchantcode;
		this.merchantordernum = merchantordernum;
		this.merchantcost = merchantcost;
		this.merchantdeal = merchantdeal;
		this.merchantpay = merchantpay;
		this.aisleid = aisleid;
		this.aislename = aislename;
		this.agentid = agentid;
		this.agentordernum = agentordernum;
		this.agentincome = agentincome;
		this.channelid = channelid;
		this.channelname = channelname;
		this.channelordernum = channelordernum;
		this.channelcost = channelcost;
		this.channeldeal = channeldeal;
		this.channelpay = channelpay;
		this.accname = accname;
		this.accnumer = accnumer;
		this.bankaddress = bankaddress;
		this.amount = amount;
		this.status = status;
		this.successtime = successtime;
		this.backlong = backlong;
		this.imgurl = imgurl;
		this.income = income;
		this.version = version;
	}
}