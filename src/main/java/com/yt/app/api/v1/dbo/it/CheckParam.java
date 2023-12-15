package com.yt.app.api.v1.dbo.it;

/**
 * <p>
 * 参数校验
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/8/1 18:08
 */
public interface CheckParam {

	/**
	 * 传入参数验证
	 *
	 * @throws ParameterException 参数异常
	 */
	void checkParam();

}
