package com.yt.app.api.v1.entity;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-03 19:50:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}