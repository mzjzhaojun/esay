package com.yt.app.api.v1.dbo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.yt.app.api.v1.dbo.base.BaseDTO;

/**
 * <p>
 * 系统管理-角色关联所有权限-保存参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/10 15:00
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统管理-角色关联所有权限-保存参数")
public class SysRoleRePermSaveDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "角色ID")
	private Long roleId;

	@ApiModelProperty(value = "有权限的菜单ids")
	private List<Long> menuIdList;

	@ApiModelProperty(value = "数据权限ids")
	private List<Long> scopeIdList;

}
