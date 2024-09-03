package com.yt.app.api.v1.dbo;

import com.yt.app.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 系统管理 - 角色管理查询参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 16:35
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleBaseDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Long excludeRoleId;

	private Boolean isRefreshAllTenant;

}
