package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

import com.yt.app.common.base.BaseDTO;

import java.util.List;

/**
 * <p>
 * 保存角色关联数据权限信息参数
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
public class SysRoleReScopeSaveDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "角色ID不能为空！")
	private Long roleId;

	private List<Long> scopeIdList;

}
