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
 * @version v1 @createdate2024-08-21 14:30:58
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
	Object realtimeexchange;
	Object dowpoint;
	Object amount;
	String accname;
	String accnumber;
	Object exchange;
	Object merchantexchange;
	Object amountreceived;
	Object usdtval;
	Integer status;
	String imgurl;
	String remark;
	Integer version;
}