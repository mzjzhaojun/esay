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
 * @version v1 @createdate2024-09-06 16:03:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tron extends YtBaseEntity<Tron> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String privatekey;
	String hexaddress;
	String address;
	Boolean status;
	String mnemoniccode;
	Object balance;
	String remark;
	Integer version;
}