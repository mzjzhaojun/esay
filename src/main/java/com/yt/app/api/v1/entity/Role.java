package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */
@Getter
@Setter
@TableName("t_sys_role")
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

	public Role() {
	}

	public Role(Long id, Long tenant_id, Long parent_id, String name, String code, Integer status, Boolean is_fixed,
			Integer sort, Boolean is_refresh_all_tenant, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.parent_id = parent_id;
		this.name = name;
		this.code = code;
		this.status = status;
		this.is_fixed = is_fixed;
		this.sort = sort;
		this.is_refresh_all_tenant = is_refresh_all_tenant;
		this.version = version;
	}
}