package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:46:43
 */
@Getter
@Setter
public class Tgconfig extends YtBaseEntity<Tgconfig> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String setname;
	String setvalues;
	String fieldname;
	String remark;
	Integer version;

	public Tgconfig() {
	}

	public Tgconfig(Long id, Long tenant_id, String setname, String setvalues, String fieldname, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.setname = setname;
		this.setvalues = setvalues;
		this.fieldname = fieldname;
		this.remark = remark;
		this.version = version;
	}
}