package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 20:07:25
 */
@Getter
@Setter
public class Systemaccountrecord extends YtBaseEntity<Systemaccountrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long systemaccountid;
	String name;
	Integer type;
	String typename;
	Double pretotalincome;
	Double prewithdrawamount;
	Double posttotalincome;
	Double postwithdrawamount;
	Double amount;
	Double balance;

	Double usdtpretotalincome;
	Double usdtprewithdrawamount;
	Double usdtposttotalincome;
	Double usdtpostwithdrawamount;
	Double usdtamount;
	Double usdtbalance;

	String remark;
	Integer version;

	public Systemaccountrecord() {
	}

	public Systemaccountrecord(Long id, Long tenant_id, Long systemaccountid, String name, Integer type,
			Double pretotalincome, Double prewithdrawamount, Double posttotalincome, Double postwithdrawamount,
			Double amount, java.util.Date update_time, Long update_by, java.util.Date create_time, Long create_by,
			String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.systemaccountid = systemaccountid;
		this.name = name;
		this.type = type;
		this.pretotalincome = pretotalincome;
		this.prewithdrawamount = prewithdrawamount;
		this.posttotalincome = posttotalincome;
		this.postwithdrawamount = postwithdrawamount;
		this.amount = amount;
		this.remark = remark;
		this.version = version;
	}
}