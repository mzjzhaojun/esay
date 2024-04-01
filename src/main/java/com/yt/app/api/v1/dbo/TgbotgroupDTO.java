package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-01 21:36:39
 */
@Getter
@Setter
public class TgbotgroupDTO {

	Long id;
	Long tenant_id;
	String tgid;
	String tgname;
	String gmanger;
	String xmanger;
	Boolean tmexchange;
	Object exchange;
	Object cost;
	Boolean status;
	String welcomemsg;
	String checkmsg;
	String startmsg;
	String endmsg;
	Integer type;
	String customersvc;
	String customersvccode;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

	public TgbotgroupDTO() {
	}

	public TgbotgroupDTO(Long id, Long tenant_id, String tgid, String tgname, String gmanger, String xmanger,
			Boolean tmexchange, Object exchange, Object cost, Boolean status, String welcomemsg, String checkmsg,
			String startmsg, String endmsg, Integer type, String customersvc, String customersvccode, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.tgid = tgid;
		this.tgname = tgname;
		this.gmanger = gmanger;
		this.xmanger = xmanger;
		this.tmexchange = tmexchange;
		this.exchange = exchange;
		this.cost = cost;
		this.status = status;
		this.welcomemsg = welcomemsg;
		this.checkmsg = checkmsg;
		this.startmsg = startmsg;
		this.endmsg = endmsg;
		this.type = type;
		this.customersvc = customersvc;
		this.customersvccode = customersvccode;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}