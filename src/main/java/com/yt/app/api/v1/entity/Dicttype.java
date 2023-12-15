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
public class Dicttype extends YtBaseEntity<Dicttype> {

	private static final long serialVersionUID = 1L;

	Long id;
	String code;
	String name;
	Boolean status;
	Integer sort;
	Integer is_fixed;
	Integer is_deleted;
	Integer version;

	public Dicttype() {
	}

	public Dicttype(Long id, String code, String name, Boolean status, Integer sort, Integer is_fixed,
			Integer is_deleted) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
		this.sort = sort;
		this.is_fixed = is_fixed;
		this.is_deleted = is_deleted;
	}
}