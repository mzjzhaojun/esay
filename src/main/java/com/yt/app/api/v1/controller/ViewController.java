package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.service.IncomeService;

/**
 * @author yyds
 * 
 * @version v1
 */

@Controller
@RequestMapping("/rest/v1/view")
public class ViewController {

	@Autowired
	private IncomeService service;

	@RequestMapping(value = "/income/{orderid}", method = RequestMethod.GET)
	public String income(@PathVariable String orderid, Model model, HttpServletRequest request, HttpServletResponse response) {
		Income income = service.getByOrderNum(orderid);
		model.addAttribute("orderid", orderid);
		model.addAttribute("resulturl", income.getResulturl());
		model.addAttribute("status", income.getStatus());
		return "static/zfbwapview";
	}

}
