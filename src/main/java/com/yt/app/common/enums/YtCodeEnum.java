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
	YT200(200, "操作成功！"),
	/**
	 * 错误
	 */
	YT500(500, "服务器异常，请联系网络管理员或稍后在试！"),
	/**
	 * 验证码不正确
	 */
	YT201(201, "验证码不正确！"),
	/**
	 * 请求的参数不合法异常
	 */
	YT202(202, "请求的参数不合法异常！"),
	/**
	 * 找不到对象异常
	 */
	YT203(203, "找不到对象异常！"),
	/**
	 * 类型不匹配异常
	 */
	YT204(204, "类型不匹配异常！"),
	/**
	 * 数据不匹配异常
	 */
	YT205(205, "数据不匹配异常！"),
	/**
	 * 数据运算出现异常
	 */
	YT206(206, "数据运算出现异常！"),
	/**
	 * 数组存储异常
	 */
	YT207(207, "数组存储异常！"),
	/**
	 * 数据读取异常
	 */
	YT208(208, "数据读取异常！"),
	/**
	 * 服务器响应数据异常
	 */
	YT209(209, "服务器响应数据异常！"),
	/**
	 * 数据格式化异常
	 */
	YT210(210, "数据格式化异常！"),
	/**
	 * 服务器访问安全异常
	 */
	YT211(211, "服务器访问安全异常！"),
	/**
	 * 不支持功能异常
	 */
	YT212(212, "不支持功能异常！"),
	/**
	 * 没有访问权限
	 */
	YT300(300, "没有访问权限！"),
	/**
	 * 用户名或密码错误
	 */
	YT301(301, "用户名或密码错误！"),
	/**
	 * 登录信息过期，请重新登录！
	 */
	YT302(302, "登录信息过期，请重新登录！"),
	/**
	 * 用户未登录，请登录后操作！
	 */
	YT303(303, "用户未登录，请登录后操作！"),
	/**
	 * 用户未找到！
	 */
	YT304(304, "用户未找到！"),
	/**
	 * 
	 */
	YT305(305, "创建账号数量超过了购买数量，请联系网络管理员！"),
	/**
	 * 访问的资源不存在
	 */
	YT400(400, "访问的资源不存在！"),
	/**
	 * 访问的资源不存在
	 */
	YT401(401, "已经设置的水军编号！"),
	/**
	 * 请求的参数解析异常
	 */
	YT402(402, "请求的参数解析异常！"),
	/**
	 * 非法请求
	 */
	YT501(501, "非法请求！"),
	/**
	 * 非法请求
	 */
	YT601(601, "授权码不正确，请检查后再试！"),

	/**
	 * 非法请求
	 */
	YT602(602, "用户名不正确，请检查后再试！"),
	/**
	 * 非法请求
	 */
	YT603(603, "账户已经到期,请联系网络管理员！"),
	/**
	 * 非法请求
	 */
	YT700(700, "新增失败，请联系网络管理员"),
	/**
	 * 非法请求
	 */
	YT701(701, "修改失败，请查询最新数据重新修改"),

	/**
	 * 
	 */
	YT888(888, "当前操作不成立，请刷新后重试!");

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
