package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.yt.app.common.base.BaseDTO;

/**
 * <p>
 * 系统管理-角色关联所有权限-保存参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/10 15:00
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleRePermSaveDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long roleId;

	private List<Long> menuIdList;

	private List<Long> scopeIdList;

}
