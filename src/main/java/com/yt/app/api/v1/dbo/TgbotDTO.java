package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */
@Getter
@Setter
public class TgbotDTO {

	Long id;
	Long tenant_id;
	String name;
	String token;
	String manger;
	Boolean status;
	Integer type;
	String expstr;
	java.util.Date exp_time;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

	public TgbotDTO() {
	}

	public TgbotDTO(Long id, Long tenant_id, String name, String token, String manger, Boolean status, Integer type,
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
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}