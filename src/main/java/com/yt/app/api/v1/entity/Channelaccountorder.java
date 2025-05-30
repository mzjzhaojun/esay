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
public class Channelaccountorder extends YtBaseEntity<Channelaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long channelid;
	String channelname;
	String nkname;
	String channelcode;
	Integer type;
	Double deal;
	Double amount;
	Double onecost;
	String accname;
	String accnumber;
	Double exchange;
	Double channelexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String statusname;
	String remark;
	Integer version;
	String ordernum;
	String imgurl;
}