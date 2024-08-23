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
 * @version v1 @createdate2024-08-22 23:02:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Incomemerchantaccountorder extends YtBaseEntity<Incomemerchantaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String username;
	String nkname;
	String merchantcode;
	Integer type;
	Double realtimeexchange;
	Double realamount;
	Double amount;
	Double exchange;
	Double merchantexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String resultimg;
	String remark;
	Integer version;
}