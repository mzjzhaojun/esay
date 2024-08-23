package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 23:02:54
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IncomemerchantaccountorderDTO {

	Long id;
	Long tenant_id;
	Long userid;
	String ordernum;
	Long merchantid;
	String username;
	String nkname;
	String merchantcode;
	Integer type;
	Object realtimeexchange;
	Object realamount;
	Object amount;
	Object exchange;
	Object merchantexchange;
	Object amountreceived;
	Object usdtval;
	Integer status;
	String resultimg;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}