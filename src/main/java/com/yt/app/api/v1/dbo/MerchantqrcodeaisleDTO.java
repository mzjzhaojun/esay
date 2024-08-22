package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 16:58:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantqrcodeaisleDTO {

	Long id;
	Long tenant_id;
	Long merchantid;
	Long qrcodeaisleid;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}