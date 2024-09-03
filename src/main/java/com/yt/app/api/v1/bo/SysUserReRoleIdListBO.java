package com.yt.app.api.v1.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 系统管理 - 用户关联角色ids
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 10:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserReRoleIdListBO {

	private Long user_id;

	private Long role_id;

}
