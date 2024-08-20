package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDTO {

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Object merchantcost;
	Object merchantdeal;
	Object merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Object agentincome;
	Long channelid;
	String channelname;
	String channelordernum;
	Object channelcost;
	Object channeldeal;
	Object channelpay;
	String accname;
	String accnumer;
	String bankcode;
	String bankname;
	String bankaddress;
	Object amount;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String imgurl;
	Object income;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Object channelbalance;
	Integer version;
}