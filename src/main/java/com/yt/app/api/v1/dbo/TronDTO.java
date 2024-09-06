package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 16:03:13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TronDTO {

	Long id;
	Long tenant_id;
	String privatekey;
	String hexaddress;
	String address;
	Boolean status;
	String mnemoniccode;
	Object balance;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}