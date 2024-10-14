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
import com.yt.app.api.v1.service.ScopedataService;
import com.yt.app.api.v1.dbo.SysScopeDataBaseDTO;
import com.yt.app.api.v1.entity.Scopedata;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

@RestController
@RequestMapping("/rest/v1/scopedata")
public class ScopedataController extends YtBaseEncipherControllerImpl<Scopedata, Long> {

	@Autowired
	private ScopedataService service;

	@RequestMapping(value = "/tree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> tree(YtRequestDecryptEntity<SysScopeDataBaseDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		List<Scopedata> list = service.tree(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(list));
	}
}
