package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */
@Getter
@Setter
public class TgbotgrouprecordVO extends YtBaseEntity<TgbotgrouprecordVO> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long tgid;
	String tgname;
	String gmanger;
	String xmanger;
	Boolean tmexchange;
	double exchange;
	double cost;
	Boolean status;
	double amount;
	double withdrawusdt;
	Integer type;
	String remark;
	Integer version;

	public TgbotgrouprecordVO() {
	}

	public TgbotgrouprecordVO(Long id, Long tenant_id, Long tgid, String tgname, String gmanger, String xmanger,
			Boolean tmexchange, double exchange, double cost, Boolean status, double amount, double withdrawusdt,
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
		this.remark = remark;
		this.version = version;
	}
}