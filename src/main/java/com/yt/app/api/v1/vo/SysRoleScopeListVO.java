package com.yt.app.api.v1.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;

import lombok.Setter;

/**
 * <p>
 * 系统管理-角色&数据权限关联表-列表-响应参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/10/18 14:03
 */
@Getter
@Setter
@ApiModel("系统管理-角色&数据权限关联表-列表-响应参数")
public class SysRoleScopeListVO {

	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("租户ID")
	private Integer tenantId;

	@ApiModelProperty("角色ID")
	private Long roleId;

	@ApiModelProperty("数据权限ID")
	private Long scopeId;

	public void handleData() {

	}

}
