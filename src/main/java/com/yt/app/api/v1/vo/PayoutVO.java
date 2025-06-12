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
 * @version v1 @createdate2023-11-21 09:56:42
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PayoutVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	Double merchantbalance;
	String merchantorderid;
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
	Double channelbalance;
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
	String statusname;
	java.util.Date successtime;
	Long backlong;
	String qrcode;
	Integer type;
	String typename;
	String imgurl;
	Double income;
	String notifyurl;
	Integer notifystatus;
	String notifystatusname;
	String remark;
	Integer version;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Double incomeamount;
	Double merchantincomeamount;
	Double channelincomeamount;

	// 订单总数
	Integer ordercount;
}