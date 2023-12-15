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

	public Dict() {
	}

	public Dict(Long id, Long dict_type_id, String code, String name, String value, Integer status, Integer sort,
			String remark, Integer is_deleted) {
		this.id = id;
		this.dict_type_id = dict_type_id;
		this.code = code;
		this.name = name;
		this.value = value;
		this.status = status;
		this.sort = sort;
		this.remark = remark;
		this.is_deleted = is_deleted;
	}
}