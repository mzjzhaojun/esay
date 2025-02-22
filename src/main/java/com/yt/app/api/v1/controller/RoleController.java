package com.yt.app.api.v1.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.RoleService;
import com.yt.app.api.v1.vo.SysRoleBaseVO;
import com.yt.app.api.v1.dbo.SysRoleBaseDTO;
import com.yt.app.api.v1.dbo.SysRoleRePermSaveDTO;
import com.yt.app.api.v1.entity.Role;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

@RestController
@RequestMapping("/rest/v1/role")
public class RoleController extends YtBaseEncipherControllerImpl<Role, Long> {

	@Autowired
	private RoleService service;

	@Override
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Role> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/tree", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> tree(YtRequestDecryptEntity<SysRoleBaseDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		List<SysRoleBaseVO> pagebean = service.tree(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/getscopeidlistbyroleid/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> SysRoleAllPermissionDetail(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		List<Long> list = service.selectListByRoleId(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(list));
	}

	@RequestMapping(value = "/saverolereperm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> saverolereperm(YtRequestDecryptEntity<SysRoleRePermSaveDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.saverolereperm(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

}
