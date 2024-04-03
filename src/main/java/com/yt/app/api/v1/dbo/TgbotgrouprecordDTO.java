package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */
@Getter
@Setter
public class TgbotgrouprecordDTO {

	Long id;
	Long tenant_id;
	Long tgid;
	String tgname;
	String gmanger;
	String xmanger;
	Boolean tmexchange;
	Object exchange;
	Object cost;
	Boolean status;
	Object amount;
	Object withdrawusdt;
	Integer type;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

	public TgbotgrouprecordDTO() {
	}

	public TgbotgrouprecordDTO(Long id, Long tenant_id, Long tgid, String tgname, String gmanger, String xmanger,
			Boolean tmexchange, Object exchange, Object cost, Boolean status, Object amount, Object withdrawusdt,
			Integer type, Long create_by, java.util.Date create_time, Long update_by, java.util.Date update_time,
			String remark, Integer version) {
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
		this.amount = amount;
		this.withdrawusdt = withdrawusdt;
		this.type = type;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}