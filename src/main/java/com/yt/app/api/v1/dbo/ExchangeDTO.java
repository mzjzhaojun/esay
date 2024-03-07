package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */
@Getter
@Setter
public class ExchangeDTO {

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Object merchantcost;
	Object merchantdeal;
	Object merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Object agentincome;
	Long channelid;
	String channelname;
	String channelordernum;
	Object channelcost;
	Object channeldeal;
	Object channelpay;
	String accname;
	String accnumer;
	String bankcode;
	String bankname;
	String bankaddress;
	Object amount;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String imgurl;
	Object income;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Object channelbalance;
	Integer version;

	public ExchangeDTO() {
	}

	public ExchangeDTO(Long id, Long tenant_id, Long userid, String ordernum, Long merchantid, String merchantname,
			String merchantcode, String merchantordernum, Object merchantcost, Object merchantdeal, Object merchantpay,
			Long aisleid, String aislename, Long agentid, String agentordernum, Object agentincome, Long channelid,
			String channelname, String channelordernum, Object channelcost, Object channeldeal, Object channelpay,
			String accname, String accnumer, String bankcode, String bankname, String bankaddress, Object amount,
			Integer status, java.util.Date successtime, Long backlong, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, String imgurl, Object income, String notifyurl,
			Integer notifystatus, String remark, Object channelbalance, Integer version) {
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
		this.bankcode = bankcode;
		this.bankname = bankname;
		this.bankaddress = bankaddress;
		this.amount = amount;
		this.status = status;
		this.successtime = successtime;
		this.backlong = backlong;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.imgurl = imgurl;
		this.income = income;
		this.notifyurl = notifyurl;
		this.notifystatus = notifystatus;
		this.remark = remark;
		this.channelbalance = channelbalance;
		this.version = version;
	}
}