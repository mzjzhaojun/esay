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
public class Qrcodeaccountorder extends YtBaseEntity<Qrcodeaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long channelid;
	String channelname;
	String nkname;
	String channelcode;
	String ordernum;
	Integer type;
	Double deal;
	Double onecost;
	Double amount;
	String accname;
	String accnumber;
	Double exchange;
	Double channelexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String remark;
	Integer version;
	String imgurl;
}