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
 * @version v1 @createdate2023-11-01 20:08:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tenantpackage extends YtBaseEntity<Tenantpackage> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	Boolean status;
	Object menu_id_list;
	String remark;
	Integer sort;
	Boolean is_deleted;

}