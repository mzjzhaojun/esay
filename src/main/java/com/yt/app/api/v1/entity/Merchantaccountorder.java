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
 * @version v1 @createdate2024-08-23 23:31:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Merchantaccountorder extends YtBaseEntity<Merchantaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String username;
	String ordernum;
	Long merchantid;
	String nkname;
	Integer type;
	String merchantcode;
	Double onecost;
	Double deal;
	Double amount;
	Double exchange;
	String accnumber;
	String accname;
	Double merchantexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String imgurl;
	String remark;
	Double incomeamount;
	Integer version;
}