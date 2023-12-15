package com.yt.app.common.enums;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 是/否
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/12 0:01
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum {

	/**
	 * 是
	 */
	YES(1, "是"),
	/**
	 * 否
	 */
	NO(0, "否");

	/**
	 * 类型
	 */
	private final Integer value;
	/**
	 * 描述
	 */
	private final String desc;

	private static final List<YesNoEnum> LIST = Lists.newArrayList();

	static {
		LIST.addAll(Arrays.asList(YesNoEnum.values()));
	}

}
