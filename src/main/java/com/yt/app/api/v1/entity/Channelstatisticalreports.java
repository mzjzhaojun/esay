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
 * @version v1 @createdate2024-09-02 12:03:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Channelstatisticalreports extends YtBaseEntity<Channelstatisticalreports> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long merchantid;
	Double todayincome;
	Double incomecount;
	Double incomeuserpaycount;
	Double incomeuserpaysuccesscount;
	Double balance;
	Integer todayorder;
	Integer successorder;
	Double todayorderamount;
	Double todaysuccessorderamount;
	Integer type;
	String remark;
	Integer version;
}