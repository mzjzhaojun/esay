package com.yt.app.api.v1.dbo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yt.app.api.v1.dbo.base.BasePageDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 系统管理-数据权限-base-请求参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/10/18 14:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统管理-数据权限-base-请求参数")
public class SysScopeDataBaseDTO extends BasePageDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("菜单ID")
	private Long menuId;

	@ApiModelProperty("权限名称")
	private String scopeName;

	/**
	 * {@link com.zhengqing.common.db.enums.DataPermissionTypeEnum}
	 */
	@ApiModelProperty("规则类型")
	private Integer scopeType;

	@JsonIgnore
	@ApiModelProperty("租户关联的菜单ids")
	private List<Integer> tenantReMenuIdList;

}
