package com.yt.app.api.v1.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 角色 -- url/btn权限
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/10 22:03
 */
@Getter
@Setter
public class SysRoleReBtnPermListVO {

	@ApiModelProperty(value = "权限描述")
	private String name;

	@ApiModelProperty(value = "菜单ID")
	private Integer menuId;

	@ApiModelProperty(value = "url权限标识")
	private String path;

	@ApiModelProperty(value = "按钮权限标识")
	private String btnPerm;

	@ApiModelProperty(value = "角色编码")
	private List<String> roleCodeList;

}
