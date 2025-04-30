package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-30 13:30:55
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysokxDTO {

	Long id;
	String name;
	Object price;
	String type;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}