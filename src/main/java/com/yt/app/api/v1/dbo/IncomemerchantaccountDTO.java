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
public class IncomemerchantaccountDTO {

	Long id;
	Long tenant_id;
	Long userid;
	Long merchantid;
	Object totalincome;
	Object toincomeamount;
	Object withdrawamount;
	Object towithdrawamount;
	Object balance;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}