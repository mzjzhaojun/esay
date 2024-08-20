package com.yt.app.api.v1.dbo;

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
 * 菜单权限树
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 20:54
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysMenuTreeDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("排除指定菜单id下级的数据")
	private Long excludeMenuId;

	@ApiModelProperty("角色ids")
	private List<Long> roleIdList;

	@ApiModelProperty("菜单ids")
	private List<Long> menuIdList;

	@ApiModelProperty("子菜单ids(返回包含子菜单的树形数据) -- 仅在逻辑层判断")
	private List<Long> childMenuIdList;

	@ApiModelProperty("是否仅显示带权限的数据  默认false:显示租户/角色下所有权限 true/null:只显示角色有的权限（适用于用户获取角色权限）")
	private Boolean isOnlyShowPerm;

	@ApiModelProperty("父id")
	private Long parentId;

	/**
	 * {@link com.zhengqing.system.enums.SysMenuTypeEnum}
	 */
	@ApiModelProperty(value = "类型")
	private Integer type;

	@ApiModelProperty(value = "是否只要系统管理员有的权限(租户套餐查询菜单 | 系统管理员获取他自己的权限 时 使用)")
	private Boolean isOnlySystemAdminRePerm;

}
