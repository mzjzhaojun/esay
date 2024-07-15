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
	Double usdcount;
	Integer todaycountorder;
	Double todayusdcount;
	Integer countorder;
	String adminmangers;
	String customermangers;
	String mangers;
	String remark;
	Integer version;

	public Tgchannelgroup() {
	}

}