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
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtBody;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.MenuService;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.entity.Menu;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

@RestController
@RequestMapping("/rest/v1/menu")
public class MenuController extends YtBaseEncipherControllerImpl<Menu, Long> {

	@Autowired
	private MenuService service;

	@RequestMapping(value = "/tree", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> tree(YtRequestDecryptEntity<SysMenuTreeDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		List<SysMenuTreeVO> list = service.tree(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(list));
	}
}
