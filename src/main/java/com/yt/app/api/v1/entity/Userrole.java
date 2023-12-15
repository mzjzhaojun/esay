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
public class Userrole extends YtBaseEntity<Userrole> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long user_id;
	Long role_id;
	Integer version;

	public Userrole() {
	}

	public Userrole(Long user_id, Long role_id) {
		this.user_id = user_id;
		this.role_id = role_id;
	}

	public Userrole(Long id, Long tenant_id, Long user_id, Long role_id, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.user_id = user_id;
		this.role_id = role_id;
		this.version = version;
	}
}