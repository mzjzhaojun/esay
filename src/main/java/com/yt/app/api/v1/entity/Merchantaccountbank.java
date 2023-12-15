package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:42:46
 */
@Getter
@Setter
public class Merchantaccountbank extends YtBaseEntity<Merchantaccountbank> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String username;
	Integer type;
	String typename;
	Boolean status;
	String accname;
	String accnumber;
	String remark;
	Integer version;

	public Merchantaccountbank() {
	}

	public Merchantaccountbank(Long id, Long tenant_id, Long userid, String username, Integer type, Boolean status,
			String accname, String accnumber, String remark, Integer version, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.username = username;
		this.type = type;
		this.status = status;
		this.accname = accname;
		this.accnumber = accnumber;
		this.remark = remark;
		this.version = version;
	}
}