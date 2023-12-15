package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-23 20:33:10
 */
@Getter
@Setter
public class Tenant extends YtBaseEntity<Tenant> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	Long admin_user_id;
	String admin_name;
	String admin_phone;
	Boolean status;
	Long package_id;
	java.util.Date expire_time;
	Integer account_count;
	Integer job_num;
	Integer sort;
	Integer is_deleted;
	Integer version;

	String username;

	String password;

	public Tenant() {
	}

	public Tenant(Long id, String name, Long admin_user_id, String admin_name, String admin_phone, Boolean status,
			Long package_id, java.util.Date expire_time, Integer account_count, Integer job_num, Integer sort,
			Integer is_deleted, Integer version) {
		this.id = id;
		this.name = name;
		this.admin_user_id = admin_user_id;
		this.admin_name = admin_name;
		this.admin_phone = admin_phone;
		this.status = status;
		this.package_id = package_id;
		this.expire_time = expire_time;
		this.account_count = account_count;
		this.job_num = job_num;
		this.sort = sort;
		this.is_deleted = is_deleted;
		this.version = version;
	}
}