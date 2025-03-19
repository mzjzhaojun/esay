package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-03-19 14:56:50
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BlocklistDTO {

	Long id;
	Long tenant_id;
	Long merchantid;
	String ordernum;
	String hexaddress;
	Boolean status;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}