package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-02 20:38:10
 */
@Getter
@Setter
public class Config extends YtBaseEntity<Config> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String keyn;
	Object valuen;
	String remark;
	Integer type;
	Integer is_deleted;
	Integer version;

	public Config() {
	}

	public Config(Long id, Long tenant_id, String key, Object value, String remark, Integer type, Integer is_deleted) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.keyn = key;
		this.valuen = value;
		this.remark = remark;
		this.type = type;
		this.is_deleted = is_deleted;
	}
}