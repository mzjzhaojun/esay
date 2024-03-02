package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-02 18:47:41
 */
@Getter
@Setter
public class Twitter extends YtBaseEntity<Twitter> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Integer type;
	String label;
	String name;
	String code;
	String remark;
	Long create_by;
	java.util.Date create_time;
	String img;
	Integer version;

	public Twitter() {
	}

	public Twitter(Long id, Long tenant_id, Integer type, String label, String name, String code, String remark,
			Long create_by, java.util.Date create_time, String img, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.type = type;
		this.label = label;
		this.name = name;
		this.code = code;
		this.remark = remark;
		this.create_by = create_by;
		this.create_time = create_time;
		this.img = img;
		this.version = version;
	}
}