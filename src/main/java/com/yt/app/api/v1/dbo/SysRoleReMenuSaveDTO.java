package com.yt.app.api.v1.dbo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <p>
 * 保存角色菜单权限信息传入参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/14 11:15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("保存角色菜单权限信息传入参数")
public class SysRoleReMenuSaveDTO {

	@ApiModelProperty(value = "角色ID")
	private Long roleId;

	@ApiModelProperty(value = "角色可访问的菜单ids")
	private List<Long> menuIdList;

}
