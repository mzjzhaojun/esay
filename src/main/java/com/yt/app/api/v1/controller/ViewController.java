package com.yt.app.api.v1.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import io.swagger.annotations.ApiOperation;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2018-09-27 09:52:46
 */

@Controller
@RequestMapping("/rest/v1/view")
public class ViewController {

	// index
	@ApiOperation(value = "index")
	@RequestMapping(value = "/index", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String submitqrcode() {
		System.out.println("======================");
		return "static/index";
	}

}
