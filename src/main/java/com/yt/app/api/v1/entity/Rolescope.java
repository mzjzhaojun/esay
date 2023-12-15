package com.yt.app.api.v1.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */
@Getter
@Setter
@Builder
public class Rolescope extends YtBaseEntity<Rolescope> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long role_id;
	Long scope_id;
	Integer version;

	public Rolescope() {
	}

	public Rolescope(Long id, Long tenant_id, Long role_id, Long scope_id, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.role_id = role_id;
		this.scope_id = scope_id;
		this.version = version;
	}
}