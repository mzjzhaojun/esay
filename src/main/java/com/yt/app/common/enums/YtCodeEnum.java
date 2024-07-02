package com.yt.app.common.enums;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常消息
 * 
 * @author dj
 *
 */
@Getter
@AllArgsConstructor
public enum YtCodeEnum {

	/**
	 * 正确
	 */
	YT200(200, "成功"),
	/**
	 * 错误
	 */
	YT500(500, "失败，服务器异常稍后在试"),
	/**
	 * 访问的资源不存在
	 */
	YT401(401, "用户未登录"),

	/**
	 * 访问的资源不存在
	 */
	YT402(402, "IP不在白名单中"),

	/**
	 * 
	 */
	YT888(888, "提示");

	@EnumValue
	private final Integer code;
	/**
	 * 类型描述 标识前端展示
	 */
//    @JsonValue
	private final String desc;

	private static final List<YtCodeEnum> LIST = Lists.newArrayList();

	static {
		LIST.addAll(Arrays.asList(YtCodeEnum.values()));
	}

	/**
	 * 根据指定的规则类型查找相应枚举类
	 *
	 * @param type 类型
	 * @return 类型枚举信息
	 * @author zhengqingya
	 * @date 2022/1/10 12:52
	 */
	public static YtCodeEnum getEnum(Integer type) {
		for (YtCodeEnum itemEnum : LIST) {
			if (itemEnum.getCode().equals(type)) {
				return itemEnum;
			}
		}
		return YT888;
	}

}
