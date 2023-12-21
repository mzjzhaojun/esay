package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:44:01
 */
@Getter
@Setter
public class Agentaccountapplyjourna extends YtBaseEntity<Agentaccountapplyjourna> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String agentname;
	String ordernum;
	Integer type;
	String typename;
	Double pretotalincome;
	Double prewithdrawamount;
	Double pretowithdrawamount;
	Double pretoincomeamount;
	Double posttotalincome;
	Double postwithdrawamount;
	Double posttowithdrawamount;
	Double posttoincomeamount;
	String remark;
	Integer version;

	public Agentaccountapplyjourna() {
	}

	public Agentaccountapplyjourna(Long id, Long tenant_id, Long userid, String agentname, String ordernum,
			Integer type, Double pretotalincome, Double prewithdrawamount, Double pretowithdrawamount,
			Double pretoincomeamount, Double posttotalincome, Double postwithdrawamount, Double posttowithdrawamount,
			Double posttoincomeamount, String remark, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.agentname = agentname;
		this.ordernum = ordernum;
		this.type = type;
		this.pretotalincome = pretotalincome;
		this.prewithdrawamount = prewithdrawamount;
		this.pretowithdrawamount = pretowithdrawamount;
		this.pretoincomeamount = pretoincomeamount;
		this.posttotalincome = posttotalincome;
		this.postwithdrawamount = postwithdrawamount;
		this.posttowithdrawamount = posttowithdrawamount;
		this.posttoincomeamount = posttoincomeamount;
		this.remark = remark;
		this.version = version;
	}
}