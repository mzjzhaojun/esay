package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.NoArgsConstructor;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}