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
 * @version v1 @createdate2024-08-22 16:58:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Merchantqrcodeaisle extends YtBaseEntity<Merchantqrcodeaisle> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long merchantid;
	Long qrcodeaisleid;
	Integer version;
}