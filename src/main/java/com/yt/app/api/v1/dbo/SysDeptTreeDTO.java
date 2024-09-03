package com.yt.app.api.v1.dbo;

import com.yt.app.common.base.BaseDTO;

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
public class SysDeptTreeDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Long excludeDeptId;

	private Long parentId;

}
