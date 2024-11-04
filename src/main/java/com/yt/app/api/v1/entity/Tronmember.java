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
 * @version v1 @createdate2024-09-08 01:31:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tronmember extends YtBaseEntity<Tronmember> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long tgid;
	Long userid;
	String password;
	String name;
	Double usdtbalance;
	Double trxbalance;
	Double sunbalance;
	Double netbalance;
	Integer integral;
	String address;
	String remark;
	Integer version;
}