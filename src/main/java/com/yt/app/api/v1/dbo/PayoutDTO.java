package com.yt.app.api.v1.dbo;




import com.yt.app.api.v1.dbo.base.BaseDTO;

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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PayoutDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
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
	String imgurl;
	Double income;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Integer version;
}