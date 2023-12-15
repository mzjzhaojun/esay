package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */
@Getter
@Setter
public class Agent extends YtBaseEntity<Agent> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String username;
	String password;
	String nkname;
	String remark;
	Double balance;
	Boolean status;
	Double exchange;
	Double onecost;
	Double downpoint;
	Integer downmerchantcount;
	String ipaddress;
	Long tenant_id;
	Long userid;
	Integer version;

	public Agent() {
	}

	public Agent(Long id, String name, String nkname, String remark, Double balance, Boolean status, Double exchange,
			Double onecost, Double downpoint, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String ipaddress, Long tenant_id, Integer version) {
		this.id = id;
		this.name = name;
		this.nkname = nkname;
		this.remark = remark;
		this.balance = balance;
		this.status = status;
		this.exchange = exchange;
		this.onecost = onecost;
		this.downpoint = downpoint;
		this.ipaddress = ipaddress;
		this.tenant_id = tenant_id;
		this.version = version;
	}
}