package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 01:44:43
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TronaddressDTO {

	Long id;
	Long tenant_id;
	Long privatekey;
	String hexaddress;
	String address;
	Boolean status;
	Integer sort;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}