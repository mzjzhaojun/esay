package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-08 01:31:33
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TronmemberDTO {

	Long id;
	Long tenant_id;
	Long tgid;
	String name;
	Object usdtbalance;
	Object trxbalance;
	Object sunbalance;
	Object netbalance;
	Integer integral;
	String address;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}