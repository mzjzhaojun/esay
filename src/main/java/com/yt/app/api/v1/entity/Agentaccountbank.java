package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:39:42
 */
@Getter
@Setter
public class Agentaccountbank extends YtBaseEntity<Agentaccountbank> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long agentaccountid;
	String username;
	Integer type;
	String typename;
	String accname;
	String accnumber;
	Boolean status;
	String remark;
	Integer version;

	public Agentaccountbank() {
	}

	public Agentaccountbank(Long id, Long tenant_id, Long userid, Long agentaccountid, String username, Integer type,
			String accname, String accnumber, Boolean status, String remark, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.agentaccountid = agentaccountid;
		this.username = username;
		this.type = type;
		this.accname = accname;
		this.accnumber = accnumber;
		this.status = status;
		this.remark = remark;
		this.version = version;
	}
}