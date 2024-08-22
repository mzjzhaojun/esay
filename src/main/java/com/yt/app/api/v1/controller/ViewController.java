package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2018-09-27 09:52:46
 */

@Controller
@RequestMapping("/rest/v1/view")
public class ViewController {

	// index
	@RequestMapping(value = "/income/{id}", method = RequestMethod.GET)
	public String submitqrcode(@PathVariable Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		model.addAttribute("test", id);
		System.out.println("======================" + id);
		return "static/income";
	}

}
