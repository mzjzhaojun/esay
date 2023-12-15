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
public class Channelaccountapplyjourna extends YtBaseEntity<Channelaccountapplyjourna> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String channelname;
	Long userid;
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

	public Channelaccountapplyjourna() {
	}

	public Channelaccountapplyjourna(Long id, Long tenant_id, String channelname, Long userid, String ordernum,
			Integer type, Double pretotalincome, Double prewithdrawamount, Double pretowithdrawamount,
			Double pretoincomeamount, Double posttotalincome, Double postwithdrawamount, Double posttowithdrawamount,
			Double posttoincomeamount, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.channelname = channelname;
		this.userid = userid;
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