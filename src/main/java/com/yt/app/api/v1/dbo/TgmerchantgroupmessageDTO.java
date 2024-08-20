package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-02-26 11:59:44
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgmerchantgroupmessageDTO {

	Long id;
	Long tenant_id;
	String message;
	Integer type;
	Object labelids;
	java.util.Date senttime;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}