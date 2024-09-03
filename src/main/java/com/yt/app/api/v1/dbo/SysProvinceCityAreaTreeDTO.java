package com.yt.app.api.v1.dbo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.yt.app.api.v1.dbo.it.HandleParam;
import com.yt.app.common.base.BaseDTO;
import com.yt.app.common.enums.SysProvinceCityAreaTypeEnum;

/**
 * <p>
 * 系统管理-省市区-树-请求参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/09/14 11:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysProvinceCityAreaTreeDTO extends BaseDTO implements HandleParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean isShop;

	/**
	 * {@link com.zhengqing.system.enums.SysProvinceCityAreaTypeEnum}
	 */
	@Max(3)
	@Min(1)
	private Integer type;

	@Override
	public void handleParam() {
		if (this.type == null) {
			this.type = SysProvinceCityAreaTypeEnum.PROVINCE.getType();
		}
	}
}
