package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */
@Getter
@Setter
public class TgmerchantgroupDTO {

	Long id;
	Long tenant_id;
	Long channelid;
	String channelname;
	Long merchantid;
	String merchantname;
	String merchantcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String adminmangers;
	String mangers;
	String customermangers;
	Object cost;
	Boolean tmexchange;
	Object downpoint;
	Object exchange;
	Integer todaycountorder;
	Integer countorder;
	Object todaycount;
	Object count;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	String version;

	public TgmerchantgroupDTO() {
	}

	public TgmerchantgroupDTO(Long id, Long tenant_id, Long channelid, String channelname, Long merchantid,
			String merchantname, String merchantcode, Boolean status, Long tgid, String tggroupname,
			String adminmangers, String mangers, String customermangers, Object cost, Boolean tmexchange,
			Object downpoint, Object exchange, Integer todaycountorder, Integer countorder, Object todaycount,
			Object count, Long create_by, java.util.Date create_time, Long update_by, java.util.Date update_time,
			String remark, String version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.channelid = channelid;
		this.channelname = channelname;
		this.merchantid = merchantid;
		this.merchantname = merchantname;
		this.merchantcode = merchantcode;
		this.status = status;
		this.tgid = tgid;
		this.tggroupname = tggroupname;
		this.adminmangers = adminmangers;
		this.mangers = mangers;
		this.customermangers = customermangers;
		this.cost = cost;
		this.tmexchange = tmexchange;
		this.downpoint = downpoint;
		this.exchange = exchange;
		this.todaycountorder = todaycountorder;
		this.countorder = countorder;
		this.todaycount = todaycount;
		this.count = count;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}