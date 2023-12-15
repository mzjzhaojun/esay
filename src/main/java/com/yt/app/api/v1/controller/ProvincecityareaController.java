package com.yt.app.api.v1.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtBody;
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.ProvincecityareaService;
import com.yt.app.api.v1.dbo.SysProvinceCityAreaTreeDTO;
import com.yt.app.api.v1.entity.Provincecityarea;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-03 19:50:02
 */

@RestController
@RequestMapping("/rest/v1/sprovincecityarea")
public class ProvincecityareaController extends YtBaseEncipherControllerImpl<Provincecityarea, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProvincecityareaService service;

	@ApiOperation(value = "tree", response = Provincecityarea.class)
	@RequestMapping(value = "/tree", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> tree(YtRequestDecryptEntity<SysProvinceCityAreaTreeDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		List<Provincecityarea> pagebean = service.tree(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
