package com.yt.app.api.v1.vo;

import com.yt.app.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;





/**
 * <p>
 * 系统管理-角色&数据权限关联表-列表-响应参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/10/18 14:03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleScopeListVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("租户ID")
	private Integer tenantId;

	@ApiModelProperty("角色ID")
	private Long roleId;

	@ApiModelProperty("数据权限ID")
	private Long scopeId;

	public void handleData() {

	}

}
