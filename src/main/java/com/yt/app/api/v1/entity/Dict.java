package com.yt.app.api.v1.entity;

import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class Dict extends YtBaseEntity<Dict> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long dict_type_id;
	String code;
	String name;
	String value;
	Integer status;
	Integer sort;
	String remark;
	Integer is_deleted;
	Integer version;

}