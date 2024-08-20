package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


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

}