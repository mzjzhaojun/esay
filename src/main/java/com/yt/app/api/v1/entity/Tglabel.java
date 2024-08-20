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
 * @version v1 @createdate2024-07-02 20:41:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tglabel extends YtBaseEntity<Tglabel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String name;
	String remark;
	Integer version;
}