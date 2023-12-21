package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-19 13:11:56
 */
@Getter
@Setter
public class Banks extends YtBaseEntity<Banks> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String value;
	String accname;
	String accnumber;
	String bankname;
	Integer version;

	public Banks() {
	}

	public Banks(Long id, Long tenant_id, Long userid, String accname, String accnumber, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.accname = accname;
		this.accnumber = accnumber;
		this.version = version;
	}
}