package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-01 21:36:39
 */
@Getter
@Setter
public class TgbotgroupVO extends YtBaseEntity<TgbotgroupVO> {

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
	String welcomemsg;
	String checkmsg;
	String startmsg;
	String endmsg;
	Integer type;
	String customersvc;
	String customersvccode;
	String remark;
	Integer version;

	public TgbotgroupVO() {
	}

	public TgbotgroupVO(Long id, Long tenant_id, Long tgid, String tgname, String gmanger, String xmanger,
			Boolean tmexchange, double exchange, double cost, Boolean status, String welcomemsg, String checkmsg,
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
		this.remark = remark;
		this.version = version;
	}
}