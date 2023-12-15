package com.yt.app.api.v1.dbo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

import com.yt.app.api.v1.dbo.base.BaseDTO;

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
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleReScopeSaveDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "角色ID不能为空！")
	@ApiModelProperty(value = "角色ID")
	private Long roleId;

	@ApiModelProperty(value = "数据权限ids")
	private List<Long> scopeIdList;

}
