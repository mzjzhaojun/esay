package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeaccountorderDTO {

	Long id;
	Long tenant_id;
	Long userid;
	Long channelid;
	String channelname;
	String nkname;
	String channelcode;
	String ordernum;
	Integer type;
	Object deal;
	Object onecost;
	Object amount;
	String accname;
	String accnumber;
	Object exchange;
	Object channelexchange;
	Object amountreceived;
	Object usdtval;
	Integer status;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
	String imgurl;
}