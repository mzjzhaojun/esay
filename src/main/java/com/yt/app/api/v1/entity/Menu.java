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
public class Menu extends YtBaseEntity<Menu> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long parent_id;
	String name;
	String icon;
	String path;
	String btn_perm;
	Integer sort;
	String component;
	String redirect;
	Boolean is_show;
	Boolean is_show_breadcrumb;
	Integer type;
	Integer is_deleted;
	Integer version;

	public Menu() {
	}

	public Menu(Long id, Long parent_id, String name, String icon, String path, String btn_perm, Integer sort,
			String component, String redirect, Boolean is_show, Boolean is_show_breadcrumb, Integer type,
			Integer is_deleted, Integer version) {
		this.id = id;
		this.parent_id = parent_id;
		this.name = name;
		this.icon = icon;
		this.path = path;
		this.btn_perm = btn_perm;
		this.sort = sort;
		this.component = component;
		this.redirect = redirect;
		this.is_show = is_show;
		this.is_show_breadcrumb = is_show_breadcrumb;
		this.type = type;
		this.is_deleted = is_deleted;
		this.version = version;
	}
}