package com.yt.app.api.v1.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.TronrecordService;
import com.yt.app.api.v1.entity.Tronrecord;
import com.yt.app.api.v1.vo.TronrecordVO;

/**
* @author yyds
* 
* @version v1
* @createdate2024-09-08 01:31:33
*/


@RestController
@RequestMapping("/rest/v1/tronrecord")
public class TronrecordController extends YtBaseEncipherControllerImpl<Tronrecord, Long> {



@Autowired
private TronrecordService service;



@Override
@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
YtIPage<Tronrecord> list = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
return new YtResponseEncryptEntity<Object>(new YtBody(list));
}



@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
YtIPage<TronrecordVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
}
}










