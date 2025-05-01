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
 * @version v1 @createdate2025-05-01 14:00:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tgbottronrecord extends YtBaseEntity<Tgbottronrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long tgid;
	String tgname;
	Integer count;
	String sendusername;
	String lostsendusername;
	String address;
	Double usdtbalance;
	Boolean status;
	Double trxbalance;
	Integer type;
	String remark;
	Integer version;
}