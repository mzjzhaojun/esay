package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */
@Getter
@Setter
public class Tgbot extends YtBaseEntity<Tgbot> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String name;
	String token;
	String manger;
	Boolean status;
	Integer type;
	String expstr;
	java.util.Date exp_time;
	String remark;
	String welcomemsg;
	Integer version;

	public Tgbot() {
	}

	public Tgbot(Long id, Long tenant_id, String name, String token, String manger, Boolean status, Integer type,
			String expstr, java.util.Date exp_time, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.name = name;
		this.token = token;
		this.manger = manger;
		this.status = status;
		this.type = type;
		this.expstr = expstr;
		this.exp_time = exp_time;
		this.remark = remark;
		this.version = version;
	}
}