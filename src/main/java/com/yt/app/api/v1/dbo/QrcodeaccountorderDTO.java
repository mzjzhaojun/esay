package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:07:27
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
	Long qrcodeaisleid;
	String qrcodeaislename;
	String qrcodename;
	Long qrcodeid;
	String qrcodecode;
	String ordernum;
	Integer type;
	Object fewamount;
	Object onecost;
	Object amount;
	String realamount;
	String accnumber;
	Object exchange;
	String qrocde;
	Integer status;
	String remark;
	String imgurl;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}