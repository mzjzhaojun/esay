package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-27 21:37:32
 */
@Getter
@Setter
public class Tgchannelgroup extends YtBaseEntity<Tgchannelgroup> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long channelid;
	String channelname;
	String channelnkname;
	Boolean status;
	Long tgid;
	String tggroupname;
	Boolean pushstatus;
	String pushname;
	String mangers;
	String remark;
	Integer version;

	public Tgchannelgroup() {
	}

	public Tgchannelgroup(Long id, Long tenant_id, Long channelid, String channelname, String channelnkname,
			Boolean status, Long tgid, String tggroupname, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, Boolean pushstatus, String pushname, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.channelid = channelid;
		this.channelname = channelname;
		this.channelnkname = channelnkname;
		this.status = status;
		this.tgid = tgid;
		this.tggroupname = tggroupname;
		this.pushstatus = pushstatus;
		this.pushname = pushname;
		this.remark = remark;
		this.version = version;
	}
}