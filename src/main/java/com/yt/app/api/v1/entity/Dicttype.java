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
}