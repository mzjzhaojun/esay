package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-15 17:34:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tgfootballgroup extends YtBaseEntity<Tgfootballgroup> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Object crownagentid;
	String crownagentname;
	String crownagentcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String adminmangers;
	String mangers;
	String customermangers;
	Object cost;
	Object realtimeexchange;
	Object costcount;
	Object usdcount;
	Object count;
	Integer todaycountorder;
	Integer countorder;
	Object todayusdcount;
	Object todaycount;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	String version;
}