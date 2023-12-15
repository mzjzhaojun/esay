package com.yt.app.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 省市区类型
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 17:16
 */
@Getter
public enum SysProvinceCityAreaTypeEnum {

	PROVINCE(1), CITY(2), AREA(3);

	private final Integer type;

	private static final List<SysProvinceCityAreaTypeEnum> LIST = Arrays.asList(SysProvinceCityAreaTypeEnum.values());

	public static SysProvinceCityAreaTypeEnum getEnum(Integer type) {
		for (SysProvinceCityAreaTypeEnum itemEnum : LIST) {
			if (itemEnum.getType().equals(type)) {
				return itemEnum;
			}
		}
		return null;
	}

	private SysProvinceCityAreaTypeEnum(Integer type) {
		this.type = type;
	}

}
