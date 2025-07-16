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
 * @version v1 @createdate2025-06-07 23:16:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payoutmerchantstatisticalreports extends YtBaseEntity<Payoutmerchantstatisticalreports> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long merchantid;
	String name;
	Object balance;
	Object todayincome;
	Object incomecount;
	Integer todayorder;
	Integer successorder;
	Object todayorderamount;
	Object todaysuccessorderamount;
	Object incomeuserpaycount;
	Object incomeuserpaysuccesscount;
	Object payoutrate;
	String dateval;
	String remark;
	Integer version;
}