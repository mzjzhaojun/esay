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
 * @version v1 @createdate2023-11-21 09:56:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payout extends YtBaseEntity<Payout> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String merchantorderid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Double merchantcost;
	Double merchantdeal;
	Double merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Double agentincome;
	Long channelid;
	String channelname;
	String channelordernum;
	Double channelcost;
	Double channeldeal;
	Double channelpay;
	String accname;
	String accnumer;
	String bankname;
	String bankcode;
	String bankaddress;
	Double amount;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	String qrcode;
	Integer type;
	String imgurl;
	Double income;
	String notifyurl;
	Integer notifystatus;
	Double channelbalance;
	String remark;
	Integer version;

}