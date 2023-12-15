package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-01 20:08:23
 */
@Getter
@Setter
public class Tenantpackage extends YtBaseEntity<Tenantpackage> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	Boolean status;
	Object menu_id_list;
	String remark;
	Integer sort;
	Boolean is_deleted;

	public Tenantpackage() {
	}

	public Tenantpackage(Long id, String name, Boolean status, Object menu_id_list, String remark, Integer sort,
			Boolean is_deleted) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.menu_id_list = menu_id_list;
		this.remark = remark;
		this.sort = sort;
		this.is_deleted = is_deleted;
	}
}