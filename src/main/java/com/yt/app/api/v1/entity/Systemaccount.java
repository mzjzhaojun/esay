package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 19:55:22
 */
@Getter
@Setter
public class Systemaccount extends YtBaseEntity<Systemaccount> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Double totalincome;
	Double withdrawamount;
	Double balance;

	Double usdttotalincome;
	Double usdtwithdrawamount;
	Double usdtbalance;
	String remark;
	Long userid;
	Integer version;

	public Systemaccount() {
	}

	public Systemaccount(Long id, Long tenant_id, Double totalincome, Double withdrawamount, Double balance,
			Long create_by, java.util.Date create_time, Long update_by, java.util.Date update_time, String remark,
			Long userid, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.totalincome = totalincome;
		this.withdrawamount = withdrawamount;
		this.balance = balance;
		this.remark = remark;
		this.userid = userid;
		this.version = version;
	}
}