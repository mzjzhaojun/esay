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
 * @version v1 @createdate2024-08-23 22:50:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Qrcodeaccount extends YtBaseEntity<Qrcodeaccount> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long qrcodelid;
	Double totalincome;
	Double toincomeamount;
	Double withdrawamount;
	Double towithdrawamount;
	Double balance;
	String remark;
	Integer version;
}