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
 * @version v1 @createdate2023-11-13 10:15:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchantaisle extends YtBaseEntity<Merchantaisle> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long aisleid;
	Long merchantid;
	Long tenant_id;
	Integer version;
	String name;
	String code;
	String mname;
	String mcode;
	Boolean status;
	String nikname;
	String type;
	String typename;
	String remark;

}