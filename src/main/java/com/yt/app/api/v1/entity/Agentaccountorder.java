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
public class Agentaccountorder extends YtBaseEntity<Agentaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long agentid;
	String username;
	String nkname;
	Integer type;
	Double amount;
	String accname;
	String accnumber;
	Double exchange;
	Double agentexchange;
	Double amountreceived;
	Double usdtval;
	Double onecost;
	Double deal;
	Integer status;
	String statusname;
	String remark;
	Integer version;
	String ordernum;
	String imgurl;

	public Agentaccountorder() {
	}

	public Agentaccountorder(Long id, Long tenant_id, Long userid, Long agentid, String username, String nkname,
			Integer type, Double amount, String accname, String accnumber, Double exchange, Double agentexchange,
			Double amountreceived, Integer status, String remark, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, Integer version, String ordernum, String imgurl) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.agentid = agentid;
		this.username = username;
		this.nkname = nkname;
		this.type = type;
		this.amount = amount;
		this.accname = accname;
		this.accnumber = accnumber;
		this.exchange = exchange;
		this.agentexchange = agentexchange;
		this.amountreceived = amountreceived;
		this.status = status;
		this.remark = remark;
		this.version = version;
		this.ordernum = ordernum;
		this.imgurl = imgurl;
	}
}