package com.yt.app.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 通用状态枚举类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/12 0:01
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

	/**
	 * 开启
	 */
	ENABLE(1, "开启"),
	/**
	 * 关闭
	 */
	DISABLE(0, "关闭");

	/**
	 * 状态
	 */
	private final Integer status;
	/**
	 * 描述
	 */
	private final String desc;

}
