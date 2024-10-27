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
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.DeptService;
import com.yt.app.api.v1.vo.SysDeptTreeVO;
import com.yt.app.api.v1.dbo.SysDeptTreeDTO;
import com.yt.app.api.v1.entity.Dept;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

@RestController
@RequestMapping("/rest/v1/dept")
public class DeptController extends YtBaseEncipherControllerImpl<Dept, Long> {

	@Autowired
	private DeptService service;

	@Override
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Dept> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/tree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> tree(YtRequestDecryptEntity<SysDeptTreeDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		List<SysDeptTreeVO> tree = service.tree(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(tree));
	}

}
