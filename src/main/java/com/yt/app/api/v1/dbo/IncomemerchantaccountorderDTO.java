package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IncomemerchantaccountorderDTO {

	Long id;
	Long tenant_id;
	Long userid;
	String merchantname;
	String merchantcode;
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
	Object realamount;
	String resulturl;
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