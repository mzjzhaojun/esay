package com.yt.app.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.BaseVO;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IncomeVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	Object merchantrealtimeexchange;
	Object merchantonecost;
	Object merchantdowpoint;
	Object exchange;
	Object merchantpay;
	Long aisleid;
	String aislename;
	Long agentid;
	String agentordernum;
	Object agentincome;
	Long channelid;
	Object channelbalance;
	String channelname;
	String channelordernum;
	Object channelrealtimeexchange;
	Object channelonecost;
	Object channeldowpoint;
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
	String qrcode;
	Integer type;
	String imgurl;
	Object income;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Integer version;
}