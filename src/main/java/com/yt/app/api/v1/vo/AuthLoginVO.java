package com.yt.app.api.v1.vo;

import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 登录参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 20:55
 */
@Data
@SuperBuilder
public class AuthLoginVO {

	@ApiModelProperty("认证请求头名")
	private String tokenName;

	@ApiModelProperty("认证值")
	private String tokenValue;

	@ApiModelProperty("租戶")
	private Long tenantId;

}
