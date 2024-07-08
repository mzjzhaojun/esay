package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */
@Getter
@Setter
public class Tglabel extends YtBaseEntity<Tglabel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String name;
	String remark;
	Integer version;

	public Tglabel() {
	}

	public Tglabel(Long id, Long tenant_id, String name, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.name = name;
		this.remark = remark;
		this.version = version;
	}
}