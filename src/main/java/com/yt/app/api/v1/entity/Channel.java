package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-12 10:55:20
 */
@Getter
@Setter
public class Channel extends YtBaseEntity<Channel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String name;
	String nkname;
	String code;
	Boolean status;
	Double balance;
	Double remotebalance;
	Double exchange;
	Double onecost;
	Double exchangeonecost;
	Double exchangedownpoint;
	Double downpoint;
	Double costexchange;
	Integer min;
	Integer max;
	Integer weight;
	Integer mtorder;
	Boolean ifordernum;
	Boolean firstmatch;
	String firstmatchmoney;
	String apiip;
	String apikey;
	String apireusultip;
	String remark;
	Integer version;

	public Channel() {
	}

	public Channel(Long id, String name, String nkname, String code, Boolean status, Double balance, Double exchange,
			Double onecost, Double downpoint, Double costexchange, Integer min, Integer max, Integer weight,
			Integer mtorder, Boolean ifordernum, Boolean firstmatch, String firstmatchmoney, String apiip,
			String apikey, String apireusultip, Long tenant_id, String remark, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.name = name;
		this.nkname = nkname;
		this.code = code;
		this.status = status;
		this.balance = balance;
		this.exchange = exchange;
		this.onecost = onecost;
		this.downpoint = downpoint;
		this.costexchange = costexchange;
		this.min = min;
		this.max = max;
		this.weight = weight;
		this.mtorder = mtorder;
		this.ifordernum = ifordernum;
		this.firstmatch = firstmatch;
		this.firstmatchmoney = firstmatchmoney;
		this.apiip = apiip;
		this.apikey = apikey;
		this.apireusultip = apireusultip;
		this.tenant_id = tenant_id;
		this.remark = remark;
		this.version = version;
	}
}