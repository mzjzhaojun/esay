package com.yt.app.api.v1.dbo;

import com.yt.app.common.base.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 系统管理-部门-树-请求参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/10/09 18:10
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统管理-部门-树-请求参数")
public class SysDeptTreeDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("部门名称")
	private String name;

	@ApiModelProperty("排除指定部门id下级的数据")
	private Long excludeDeptId;

	@ApiModelProperty("父id")
	private Long parentId;

}
