package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */
@Getter
@Setter
public class Channelaccountorder extends YtBaseEntity<Channelaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long channelid;
	String username;
	String nkname;
	String channelcode;
	Integer type;
	Double amount;
	String accname;
	String accnumber;
	Double exchange;
	Double channelexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String statusname;
	String remark;
	Integer version;
	String ordernum;
	String imgurl;

	public Channelaccountorder() {
	}

	public Channelaccountorder(Long id, Long tenant_id, Long userid, Long channelid, String username, String nkname,
			String channelcode, Integer type, Double amount, String accname, String accnumber, Double exchange,
			Double channelexchange, Double amountreceived, Integer status, String remark, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, Integer version, String ordernum,
			String imgurl) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.channelid = channelid;
		this.username = username;
		this.nkname = nkname;
		this.channelcode = channelcode;
		this.type = type;
		this.amount = amount;
		this.accname = accname;
		this.accnumber = accnumber;
		this.exchange = exchange;
		this.channelexchange = channelexchange;
		this.amountreceived = amountreceived;
		this.status = status;
		this.remark = remark;
		this.version = version;
		this.ordernum = ordernum;
		this.imgurl = imgurl;
	}
}