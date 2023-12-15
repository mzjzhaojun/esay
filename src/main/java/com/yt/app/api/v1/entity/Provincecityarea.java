package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-03 19:50:01
 */
@Getter
@Setter
public class Provincecityarea implements Serializable {

	private static final long serialVersionUID = 1L;

	Long id;
	Long parent_id;
	String parent_code;
	String name;
	String code;
	Integer type;
	Integer is_shop;
	Integer version;
	List<Provincecityarea> children;

	public Provincecityarea() {
	}

	public Provincecityarea(Long id, Long parent_id, String parent_code, String name, String code, Integer type,
			Integer is_shop, Integer version) {
		this.id = id;
		this.parent_id = parent_id;
		this.parent_code = parent_code;
		this.name = name;
		this.code = code;
		this.type = type;
		this.is_shop = is_shop;
		this.version = version;
	}
}