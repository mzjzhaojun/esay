package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */
@Getter
@Setter
public class TglabelDTO {

	Long id;
	Long tenant_id;
	String name;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

	public TglabelDTO() {
	}

	public TglabelDTO(Long id, Long tenant_id, String name, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.name = name;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}