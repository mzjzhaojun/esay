package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */
@Getter
@Setter
public class Agentaccount extends YtBaseEntity<Agentaccount> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long agentid;
	Double totalincome;
	Double withdrawamount;
	Double towithdrawamount;
	Double toincomeamount;
	Double balance;
	List<Agentaccountbank> listbanks;
	String remark;
	Integer version;

	public Agentaccount() {
	}

	public Agentaccount(Long id, Long tenant_id, Long agentid, Double totalincome, Double withdrawamount,
			Double towithdrawamount, Double toincomeamount, Double balance, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.agentid = agentid;
		this.totalincome = totalincome;
		this.withdrawamount = withdrawamount;
		this.towithdrawamount = towithdrawamount;
		this.toincomeamount = toincomeamount;
		this.balance = balance;

		this.remark = remark;
		this.version = version;
	}
}