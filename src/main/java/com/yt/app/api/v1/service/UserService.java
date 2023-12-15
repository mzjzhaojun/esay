package com.yt.app.api.v1.service;

import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */

public interface UserService extends YtIBaseService<User, Long> {

	SysUserPermVO getUserPerm(SysUserPermDTO params);

	Integer resetpassword(User u);

}