package com.yt.app.api.v1.bo;

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

	private AuthSourceEnum authSourceEnum;

	private Long userId;

	private String username;

	private List<Long> roleIdList;

	private List<String> roleCodeList;

	private List<Long> allRoleIdList;

	private Long tenantId;

	private Long deptId;

	private Long accounttype;

	private Long systemaccountId;

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
