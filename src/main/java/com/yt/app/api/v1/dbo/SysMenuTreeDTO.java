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
 * 菜单权限树
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 20:54
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysMenuTreeDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Long excludeMenuId;

	private List<Long> roleIdList;

	private List<Long> menuIdList;

	private List<Long> childMenuIdList;

	private Boolean isOnlyShowPerm;

	private Long parentId;

	/**
	 * {@link com.zhengqing.system.enums.SysMenuTypeEnum}
	 */
	private Integer type;

	private Boolean isOnlySystemAdminRePerm;

}
