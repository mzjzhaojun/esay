package com.yt.app.api.v1.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
@NoArgsConstructor
public class AuthLoginDTO {

	@NotBlank(message = "账号不能为空!")
	private String username;

	@NotBlank(message = "密码不能为空!")
	private String password;

	@NotBlank(message = "验证码不能为空!")
	private String code;

	@NotBlank(message = "验证码不能为空!")
	private String twocode;

}
