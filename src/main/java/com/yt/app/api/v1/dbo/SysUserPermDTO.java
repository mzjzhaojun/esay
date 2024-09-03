package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.experimental.SuperBuilder;

import com.yt.app.api.v1.dbo.it.CheckParam;
import com.yt.app.common.base.BaseDTO;

/**
 * <p>
 * 系统管理 - 用户信息查询
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 10:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysUserPermDTO extends BaseDTO implements CheckParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId;

	private String username;

	@Override
	public void checkParam() {

	}

}
