package com.yt.app.api.v1.entity;

import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tgmerchantgrouplabel extends YtBaseEntity<Tgmerchantgrouplabel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String name;
	String remark;
	Integer version;
}