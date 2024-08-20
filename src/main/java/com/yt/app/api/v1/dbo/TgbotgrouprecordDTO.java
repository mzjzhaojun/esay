package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgbotgrouprecordDTO {

	Long id;
	Long tenant_id;
	Long tgid;
	String tgname;
	String gmanger;
	String xmanger;
	Boolean tmexchange;
	Object exchange;
	Object cost;
	Boolean status;
	Object amount;
	Object withdrawusdt;
	Integer type;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

}