package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channelaccount extends YtBaseEntity<Channelaccount> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long channelid;
	Double totalincome;
	Double withdrawamount;
	Double towithdrawamount;
	Double toincomeamount;
	Double balance;
	String remark;
	Integer version;
}