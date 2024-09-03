package com.yt.app.api.v1.dbo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yt.app.common.base.BasePageDTO;

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
public class SysScopeDataBaseDTO extends BasePageDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long menuId;

	private String scopeName;

	/**
	 * {@link com.zhengqing.common.db.enums.DataPermissionTypeEnum}
	 */
	private Integer scopeType;

	@JsonIgnore
	private List<Integer> tenantReMenuIdList;

}
