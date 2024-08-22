package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 14:27:18
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeaisleDTO {

	Long id;
	Long tenant_id;
	String name;
	String nikname;
	String code;
	Boolean status;
	String type;
	Integer qrcodecount;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}