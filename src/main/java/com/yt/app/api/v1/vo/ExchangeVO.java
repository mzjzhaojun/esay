package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */
@Getter
@Setter
public class ExchangeVO extends YtBaseEntity<ExchangeVO> {

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
	String statusname;
	java.util.Date successtime;
	Long backlong;
	String imgurl;
	Double income;
	String qrcode;
	Integer type;
	String typename;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Double channelbalance;
	Integer version;

	public ExchangeVO() {
	}
}