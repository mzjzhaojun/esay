package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */
@Getter
@Setter
public class Exchange extends YtBaseEntity<Exchange> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Double merchantrealtimeexchange;
	Double merchantdowpoint;
	Double merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Double agentincome;
	Long channelid;
	String channelname;
	String channelordernum;
	Double channelrealtimeexchange;
	Double channeldowpoint;
	Double channelonecost;
	Double channelpay;
	String accname;
	String accnumer;
	String bankcode;
	String bankname;
	String bankaddress;
	Double amount;
	Double exchange;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	String qrcode;
	Integer type;
	String imgurl;
	Double income;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Double channelbalance;
	Integer version;

	public Exchange() {
	}

	public Exchange(Long id, Long tenant_id, Long userid, String ordernum, Long merchantid, String merchantname,
			String merchantcode, String merchantordernum, Double merchantrealtimeexchange, Double merchantdowpoint,
			Double merchantpay, Long aisleid, String aislename, Long agentid, String agentordernum, Double agentincome,
			Long channelid, String channelname, String channelordernum, Double channelrealtimeexchange,
			Double channeldowpoint, Double channelpay, String accname, String accnumer, String bankcode,
			String bankname, String bankaddress, Double amount, Integer status, java.util.Date successtime,
			Long backlong, Long create_by, java.util.Date create_time, Long update_by, java.util.Date update_time,
			String imgurl, Double income, String notifyurl, Integer notifystatus, String remark, Double channelbalance,
			Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.ordernum = ordernum;
		this.merchantid = merchantid;
		this.merchantname = merchantname;
		this.merchantcode = merchantcode;
		this.merchantordernum = merchantordernum;
		this.merchantrealtimeexchange = merchantrealtimeexchange;
		this.merchantdowpoint = merchantdowpoint;
		this.merchantpay = merchantpay;
		this.aisleid = aisleid;
		this.aislename = aislename;
		this.agentid = agentid;
		this.agentordernum = agentordernum;
		this.agentincome = agentincome;
		this.channelid = channelid;
		this.channelname = channelname;
		this.channelordernum = channelordernum;
		this.channelrealtimeexchange = channelrealtimeexchange;
		this.channeldowpoint = channeldowpoint;
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
		this.imgurl = imgurl;
		this.income = income;
		this.notifyurl = notifyurl;
		this.notifystatus = notifystatus;
		this.remark = remark;
		this.channelbalance = channelbalance;
		this.version = version;
	}
}