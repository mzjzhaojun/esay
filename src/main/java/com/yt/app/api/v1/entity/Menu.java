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
 * @version v1 @createdate2023-10-25 17:20:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}