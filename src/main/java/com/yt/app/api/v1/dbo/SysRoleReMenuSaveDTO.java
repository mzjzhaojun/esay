package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <p>
 * 保存角色菜单权限信息传入参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/14 11:15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleReMenuSaveDTO {

	private Long roleId;

	private List<Long> menuIdList;

}
