package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import com.yt.app.common.base.YtBaseEntity;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.enums.UserSexEnum;
import com.yt.app.common.util.RedisUtil;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */
@Getter
@Setter
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

	public User() {
	}

	public User(Long id, Long tenant_id, String username, String password, String nickname, Integer sex, String phone,
			String email, String avatar_url, Long dept_id, List<Long> post_idlist, Integer is_fixed, Long accounttype,
			Integer is_deleted, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.sex = sex;
		this.phone = phone;
		this.email = email;
		this.avatar_url = avatar_url;
		this.dept_id = dept_id;
		this.post_idlist = post_idlist;
		this.is_fixed = is_fixed;
		this.accounttype = accounttype;
		this.is_deleted = is_deleted;
		this.version = version;
	}

	public void handleData() {
		this.sexName = UserSexEnum.getEnum(this.sex).getDesc();
		this.setAccounttypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + this.getAccounttype()));
	}
}