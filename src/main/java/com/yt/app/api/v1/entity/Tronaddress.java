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
 * @version v1 @createdate2024-09-06 01:44:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tronaddress extends YtBaseEntity<Tronaddress> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String privatekey;
	String hexaddress;
	String address;
	Boolean status;
	String mnemoniccode;
	String remark;
	Integer version;
}