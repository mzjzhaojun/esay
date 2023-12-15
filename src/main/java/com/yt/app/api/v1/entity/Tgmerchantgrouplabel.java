package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */
@Getter
@Setter
public class Tgmerchantgrouplabel extends YtBaseEntity<Tgmerchantgrouplabel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long groupid;
	Long labelid;
	Integer version;

	public Tgmerchantgrouplabel() {
	}

	public Tgmerchantgrouplabel(Long id, Long tenant_id, Long groupid, Long labelid, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.groupid = groupid;
		this.labelid = labelid;
		this.version = version;
	}
}