package com.yt.app.api.v1.vo;

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

	private String tokenName;

	private String tokenValue;

	private Long tenantId;

	private Long userid;

	private Long accounttype;

}
