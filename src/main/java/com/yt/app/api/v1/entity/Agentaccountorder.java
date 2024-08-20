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
 * @version v1 @createdate2023-11-18 12:41:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agentaccountorder extends YtBaseEntity<Agentaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long agentid;
	String username;
	String nkname;
	Integer type;
	Double amount;
	String accname;
	String accnumber;
	Double exchange;
	Double agentexchange;
	Double amountreceived;
	Double usdtval;
	Double onecost;
	Double deal;
	Integer status;
	String statusname;
	String remark;
	Integer version;
	String ordernum;
	String imgurl;
}