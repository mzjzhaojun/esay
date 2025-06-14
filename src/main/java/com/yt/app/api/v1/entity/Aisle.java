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
 * @version v1 @createdate2023-11-10 19:00:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Aisle extends YtBaseEntity<Aisle> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String code;
	Boolean status;
	Boolean dynamic;
	String nikname;
	Integer type;
	String typename;
	Integer channelcount;
	String remark;
	Long tenant_id;
	Integer version;
}