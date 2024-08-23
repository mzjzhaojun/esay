package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 18:22:46
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {

	Long id;
	Long tenant_id;
	Long merchantuserid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	String merchantorderid;
	Object merchantonecost;
	Object merchantpay;
	String aislecode;
	Long qrcodeaisleid;
	String qrcodeaislename;
	Long agentid;
	String agentordernum;
	Object agentincome;
	Long channelid;
	Object qrcodeid;
	String qrcodename;
	String channelordernum;
	String channeluserid;
	Object amount;
	Object realamount;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String qrcode;
	Integer type;
	String resulturl;
	Object fewamount;
	String notifyurl;
	Integer notifystatus;
	String remark;
	Integer version;
}