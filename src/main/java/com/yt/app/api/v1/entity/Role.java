package com.yt.app.api.v1.entity;

import java.util.List;

import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class Role extends YtBaseEntity<Role> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long parent_id;
	String name;
	String code;
	Integer status;
	Boolean is_fixed;
	Integer sort;
	Boolean is_refresh_all_tenant;
	Integer version;

	List<SysMenuTreeVO> menuTree;
}