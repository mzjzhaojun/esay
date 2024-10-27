package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */

public interface UserService extends YtIBaseService<User, Long> {
	
	YtIPage<User> page(Map<String, Object> param);

	SysUserPermVO getUserPerm(SysUserPermDTO params);

	Integer resetpassword(User u);

	Integer checkgoogle(Long code);

}