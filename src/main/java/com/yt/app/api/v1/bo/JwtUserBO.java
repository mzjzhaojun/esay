package com.yt.app.api.v1.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.yt.app.common.enums.AuthSourceEnum;

/**
 * <p>
 * 用户token信息
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/11/28 23:16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserBO {

	@ApiModelProperty("认证来源")
	private AuthSourceEnum authSourceEnum;

	@ApiModelProperty("用户ID")
	private Long userId;

	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("角色ids")
	private List<Long> roleIdList;

	@ApiModelProperty("角色编码")
	private List<String> roleCodeList;

	@ApiModelProperty("当前角色以及下级ids")
	private List<Long> allRoleIdList;

	@ApiModelProperty("tenantId")
	private Long tenantId;

	@ApiModelProperty(value = "部门ID")
	private Long deptId;

	@ApiModelProperty(value = "账户类型")
	private Long accounttype;

	@ApiModelProperty(value = "系统账户")
	private Long systemaccountId;

	@ApiModelProperty(value = "数据权限")
	private List<SysScopeDataBO> scopeDataList;

	/**
	 * 获取B端用户ID
	 */
	public Long getUserIdForB() {
		return Long.valueOf(this.userId);
	}

	/**
	 * 获取C端用户ID
	 */
	public Long getUserIdForC() {
		return Long.valueOf(this.userId);
	}

}
