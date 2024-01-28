package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-28 21:06:01
 */
@Getter
@Setter
public class TgmerchantgroupmessageDTO {

	Long id;
	Long tenant_id;
	String message;
	Integer type;
	Object labelids;
	java.util.Date senttime;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

	public TgmerchantgroupmessageDTO() {
	}

	public TgmerchantgroupmessageDTO(Long id, Long tenant_id, String message, Integer type, Object labelids,
			java.util.Date senttime, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.message = message;
		this.type = type;
		this.labelids = labelids;
		this.senttime = senttime;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}