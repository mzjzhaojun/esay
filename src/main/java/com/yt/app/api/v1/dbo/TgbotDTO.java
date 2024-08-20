package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgbotDTO {

	Long id;
	Long tenant_id;
	String name;
	String token;
	String manger;
	Boolean status;
	Integer type;
	String expstr;
	java.util.Date exp_time;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

}