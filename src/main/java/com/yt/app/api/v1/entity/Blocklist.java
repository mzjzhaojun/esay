package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-03-19 14:56:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Blocklist extends YtBaseEntity<Blocklist> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long merchantid;
	String ordernum;
	String hexaddress;
	String ipaddress;
	String remark;
	Integer version;
}