package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 10:33:32
 */
@Getter
@Setter
public class Sysbank extends YtBaseEntity<Sysbank> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String code;
	String spname;
	Boolean status;
	String remark;
	Integer version;

	public Sysbank() {
	}

	public Sysbank(Long id, String name, String code, String spname, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, Boolean status, String remark, Integer version) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.spname = spname;
		this.status = status;
		this.remark = remark;
		this.version = version;
	}
}