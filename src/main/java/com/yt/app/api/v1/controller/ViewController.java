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
import com.yt.app.common.resource.DictionaryResource;

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
		model.addAttribute("incomeid", income.getId());
		model.addAttribute("expiredminute", income.getExpiredminute());
		model.addAttribute("qrcode", income.getQrcode());
		model.addAttribute("amount", income.getRealamount());
		model.addAttribute("backforwardurl", income.getBackforwardurl());
		model.addAttribute("status", income.getStatus());
		if (income.getType().equals(DictionaryResource.BANK_TYPE_122.toString()))
			return "static/wxview";
		else if (income.getType().equals(DictionaryResource.BANK_TYPE_123.toString()))
			return "static/aliview";
		else if (income.getType().equals(DictionaryResource.PROJECT_TYPE_505.toString()))
			return "static/h5aliview";
		return "static/othview";
	}

	@RequestMapping(value = "/income/error", method = RequestMethod.GET)
	public String income(Model model, HttpServletRequest request, HttpServletResponse response) {
		return "static/error";
	}

}
