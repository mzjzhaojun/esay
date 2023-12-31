package com.yt.app.api.v1.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统管理 - 用户关联角色ids
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 10:48
 */
@Getter
@Setter
public class SysUserReRoleIdListBO {

	@ApiModelProperty(value = "用户ID")
	private Long user_id;

	@ApiModelProperty(value = "角色ID")
	private Long role_id;

}
