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
 * @version v1 @createdate2023-11-15 18:42:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TSysconfig extends YtBaseEntity<TSysconfig> {

	private static final long serialVersionUID = 1L;

	Long id;
	String keyn;
	String valuen;
	Long tenant_id;
	Integer version;

}