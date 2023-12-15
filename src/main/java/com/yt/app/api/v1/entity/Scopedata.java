package com.yt.app.api.v1.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */
@Getter
@Setter
@Builder
public class Scopedata extends YtBaseEntity<Scopedata> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long menu_id;
	String scope_name;
	String scope_column;
	String scope_visible_field;
	Object scope_class;
	Integer scope_type;
	String scope_value;
	String remark;
	Integer is_deleted;
	Integer version;

	String custom_id;
	String custom_name;
	List<Scopedata> children;
	String menuFullName;

	public Scopedata() {
	}

	public Scopedata(Long id, Long menu_id, String scope_name, String scope_column, String scope_visible_field,
			Object scope_class, Integer scope_type, String scope_value, String remark, Integer is_deleted,
			Integer version) {
		this.id = id;
		this.menu_id = menu_id;
		this.scope_name = scope_name;
		this.scope_column = scope_column;
		this.scope_visible_field = scope_visible_field;
		this.scope_class = scope_class;
		this.scope_type = scope_type;
		this.scope_value = scope_value;
		this.remark = remark;
		this.is_deleted = is_deleted;
		this.version = version;
	}

	public Scopedata(Long id, Long menu_id, String scope_name, String scope_column, String scope_visible_field,
			Object scope_class, Integer scope_type, String scope_value, String remark, Integer is_deleted,
			Integer version, String custom_id, String custom_name, List<Scopedata> children, String menuFullName) {
		super();
		this.id = id;
		this.menu_id = menu_id;
		this.scope_name = scope_name;
		this.scope_column = scope_column;
		this.scope_visible_field = scope_visible_field;
		this.scope_class = scope_class;
		this.scope_type = scope_type;
		this.scope_value = scope_value;
		this.remark = remark;
		this.is_deleted = is_deleted;
		this.version = version;
		this.custom_id = custom_id;
		this.custom_name = custom_name;
		this.children = children;
		this.menuFullName = menuFullName;
	}

}