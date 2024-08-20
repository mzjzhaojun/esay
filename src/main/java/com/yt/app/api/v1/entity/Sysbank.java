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
 * @version v1 @createdate2024-01-18 11:28:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sysbank extends YtBaseEntity<Sysbank> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String code;
	String spname;
	Boolean status;
	String remark;
	Integer sort;
	Integer version;

}