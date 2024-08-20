package com.yt.app.api.v1.entity;

import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exchange extends YtBaseEntity<Exchange> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Double merchantrealtimeexchange;
	Double merchantdowpoint;
	Double merchantonecost;
	Double merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Double agentincome;
	Long channelid;
	String channelname;
	String channelordernum;
	Double channelrealtimeexchange;
	Double channeldowpoint;
	Double channelonecost;
	Double channelpay;
	String accname;
	String accnumer;
	String bankcode;
	String bankname;
	String bankaddress;
	Double amount;
	Double exchange;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	String qrcode;
	Integer type;
	String imgurl;
	Double income;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Double channelbalance;
	Integer version;
}