package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgmerchantgroupDTO {

	Long id;
	Long tenant_id;
	Long channelid;
	String channelname;
	Long merchantid;
	String merchantname;
	String merchantcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String adminmangers;
	String mangers;
	String customermangers;
	Object cost;
	Boolean tmexchange;
	Object downpoint;
	Object exchange;
	Integer todaycountorder;
	Integer countorder;
	Object todaycount;
	Object count;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	String version;
}