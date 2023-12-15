package com.yt.app.api.v1.dbo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.yt.app.api.v1.dbo.base.BaseDTO;
import com.yt.app.api.v1.dbo.it.CheckParam;

/**
 * <p>
 * 系统管理 - 用户信息查询
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 10:48
 */
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class SysUserPermDTO extends BaseDTO implements CheckParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户ID")
	private Long userId;

	@ApiModelProperty(value = "账号")
	private String username;

	@Override
	public void checkParam() {

	}

}
