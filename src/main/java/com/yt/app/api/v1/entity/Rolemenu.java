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
 * @version v1 @createdate2023-10-25 17:20:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rolemenu extends YtBaseEntity<Rolemenu> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long role_id;
	Long menu_id;
	Integer version;
}