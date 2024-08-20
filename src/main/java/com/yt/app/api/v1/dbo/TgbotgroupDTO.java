package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-01 21:36:39
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgbotgroupDTO {

	Long id;
	Long tenant_id;
	String tgid;
	String tgname;
	String gmanger;
	String xmanger;
	Boolean tmexchange;
	Object exchange;
	Object cost;
	Boolean status;
	String welcomemsg;
	String checkmsg;
	String startmsg;
	String endmsg;
	Integer type;
	String customersvc;
	String customersvccode;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

}