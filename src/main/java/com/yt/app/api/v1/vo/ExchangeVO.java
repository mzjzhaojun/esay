package com.yt.app.api.v1.vo;

import com.yt.app.common.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExchangeVO extends BaseVO {

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
	Double merchantonecost;
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
}