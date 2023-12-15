package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */
@Getter
@Setter
public class Payconfig extends YtBaseEntity<Payconfig> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String usdt;
	Double exchange;
	Long tenant_id;
	Integer version;

	public Payconfig() {
	}

	public Payconfig(Long id, String name, String usdt, Double value, Long tenant_id, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.name = name;
		this.usdt = usdt;
		this.exchange = value;
		this.tenant_id = tenant_id;
		this.version = version;
	}
}