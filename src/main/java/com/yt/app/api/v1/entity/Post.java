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
public class Post extends YtBaseEntity<Post> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long dept_id;
	String deptname;
	String name;
	String code;
	Boolean status;
	Integer sort;
	String remark;
	Integer version;

	public Post() {
	}

	public Post(Long id, Long tenant_id, Long dept_id, String name, String code, Boolean status, Integer sort,
			String remark) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.dept_id = dept_id;
		this.name = name;
		this.code = code;
		this.status = status;
		this.sort = sort;
		this.remark = remark;
	}
}