package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 14:45:16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SystemstatisticalreportsDTO {

	Long id;
	Long tenant_id;
	Long userid;
	Long merchantid;
	Object todayincome;
	Object incomecount;
	Object balance;
	Integer todayorder;
	Integer successorder;
	Object todayorderamount;
	Object todaysuccessorderamount;
	Integer type;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}