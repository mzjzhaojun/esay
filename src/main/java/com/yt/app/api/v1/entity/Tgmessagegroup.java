package com.yt.app.api.v1.entity;

import java.util.List;

import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tgmessagegroup extends YtBaseEntity<Tgmessagegroup> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	List<Long> merchantids;
	List<Long> channelids;
	Long tgcid;
	Long tgmid;
	String merchantname;
	String channelname;
	Boolean status;
	String tggroupname;
	String adminmangers;
	String mangers;
	String customermangers;
	Double cost;
	Double realtimeexchange;
	Double costcount;
	Double usdcount;
	Integer todaycountorder;
	Double todayusdcount;
	Integer countorder;
	Double todaycount;
	Double count;
	String remark;
	String version;

}