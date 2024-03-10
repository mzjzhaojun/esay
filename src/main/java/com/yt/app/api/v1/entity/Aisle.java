package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */
@Getter
@Setter
public class Aisle extends YtBaseEntity<Aisle> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String code;
	Boolean status;
	String nikname;
	String type;
	String typename;
	Integer channelcount;
	String remark;
	Long tenant_id;
	Integer version;

	public Aisle() {
	}

	public Aisle(Long id, String name, String code, Boolean status, String nikname, String type, Integer channelcount,
			String remark, Long create_by, java.util.Date create_time, Long update_by, java.util.Date update_time,
			Long tenant_id, Integer version) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.status = status;
		this.nikname = nikname;
		this.type = type;
		this.channelcount = channelcount;
		this.remark = remark;
		this.tenant_id = tenant_id;
		this.version = version;
	}
}