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
 * @version v1 @createdate2023-12-27 21:37:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tgchannelgroup extends YtBaseEntity<Tgchannelgroup> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	List<Long> channelids;
	String channelname;
	String channelnkname;
	Boolean status;
	Long tgid;
	String tggroupname;
	Double usdcount;
	Integer todaycountorder;
	Double todayusdcount;
	Integer countorder;
	String adminmangers;
	String customermangers;
	String mangers;
	String remark;
	Integer version;
}