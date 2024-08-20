package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TglabelDTO {

	Long id;
	Long tenant_id;
	String name;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

}