package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:16:34
 */
@Getter
@Setter
public class Aislechannel extends YtBaseEntity<Aislechannel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long aisleid;
	Long channelid;
	Long tenant_id;
	Integer version;

	public Aislechannel() {
	}

	public Aislechannel(Long id, Long aisleid, Long channelid, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.aisleid = aisleid;
		this.channelid = channelid;
		this.version = version;
	}
}