package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:15:12
 */
@Getter
@Setter
public class Merchantaisle extends YtBaseEntity<Merchantaisle> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long aisleid;
	Long merchantid;
	Long tenant_id;
	Integer version;
	String name;
	String code;
	Boolean status;
	Double exchange;
	Double onecost;
	String remark;

	public Merchantaisle() {
	}

	public Merchantaisle(Long id, Long aisleid, Long merchantid, Long create_by, java.util.Date create_time,
			Long update_by, java.util.Date update_time, Integer version) {
		this.id = id;
		this.aisleid = aisleid;
		this.merchantid = merchantid;
		this.version = version;
	}
}