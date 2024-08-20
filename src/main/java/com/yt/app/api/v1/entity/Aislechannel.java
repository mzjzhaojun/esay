package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:16:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Aislechannel extends YtBaseEntity<Aislechannel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long aisleid;
	Long channelid;
	Long tenant_id;
	Integer version;
}