package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */
@Getter
@Setter
public class Dept extends YtBaseEntity<Dept> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long parent_id;
	String name;
	Integer sort;
	Long leader_user_id;
	String phone;
	String email;
	Boolean status;
	String province_name;
	String city_name;
	String area_name;
	String address;
	String remark;
	Integer is_deleted;
	Integer version;

	public Dept() {
	}

	public Dept(Long id, Long tenant_id, Long parent_id, String name, Integer sort, Long leader_user_id, String phone,
			String email, Boolean status, String province_name, String city_name, String area_name, String address,
			String remark, Integer is_deleted) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.parent_id = parent_id;
		this.name = name;
		this.sort = sort;
		this.leader_user_id = leader_user_id;
		this.phone = phone;
		this.email = email;
		this.status = status;
		this.province_name = province_name;
		this.city_name = city_name;
		this.area_name = area_name;
		this.address = address;
		this.remark = remark;
		this.is_deleted = is_deleted;
	}
}