package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-05-01 14:00:37
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgbottronrecordDTO {

	Long id;
	Long tenant_id;
	Long tgid;
	String tgname;
	Integer count;
	String sendusername;
	String lostsendusername;
	String address;
	Object usdtbalance;
	Boolean status;
	Object trxbalance;
	Integer type;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}