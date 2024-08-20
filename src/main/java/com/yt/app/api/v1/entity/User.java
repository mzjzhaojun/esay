package com.yt.app.api.v1.entity;

import java.util.List;

import com.yt.app.common.base.YtBaseEntity;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.enums.UserSexEnum;
import com.yt.app.common.util.RedisUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends YtBaseEntity<User> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String username;
	String twofactorcode;
	String password;
	String nickname;
	Integer sex;
	String phone;
	String email;
	String avatar_url;
	Long dept_id;
	List<Long> post_idlist;
	Integer is_fixed;
	Long accounttype;
	String accounttypename;
	Integer is_deleted;
	Integer version;

	//
	List<Long> roleIdList;
	String sexName;
	String roleNames;
	String deptName;
	String token;

	public void handleData() {
		this.sexName = UserSexEnum.getEnum(this.sex).getDesc();
		this.setAccounttypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + this.getAccounttype()));
	}
}