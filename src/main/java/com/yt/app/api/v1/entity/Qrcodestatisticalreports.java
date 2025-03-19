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
 * @version v1 @createdate2025-03-19 19:51:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Qrcodestatisticalreports extends YtBaseEntity<Qrcodestatisticalreports> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long qrcodeid;
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