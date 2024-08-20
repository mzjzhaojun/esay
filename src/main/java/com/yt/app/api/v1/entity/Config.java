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
 * @version v1 @createdate2023-11-02 20:38:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Config extends YtBaseEntity<Config> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String keyn;
	Object valuen;
	String remark;
	Integer type;
	Integer is_deleted;
	Integer version;

}