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
		if (income == null)
			return "static/error";
		model.addAttribute("orderid", orderid);
		model.addAttribute("amount", income.getAmount());
		model.addAttribute("realamount", income.getRealamount());
		model.addAttribute("backurl", income.getBackforwardurl());
		model.addAttribute("resulturl", income.getResulturl());
		model.addAttribute("status", income.getStatus());
		if (income.getQrcodecode().equals(DictionaryResource.PRODUCT_YPLWAP)) {
			return "static/yplwapview";
		}
		return "static/merchantpay";
	}

	@RequestMapping(value = "/income/error", method = RequestMethod.GET)
	public String error(HttpServletRequest request, HttpServletResponse response) {
		return "static/error";
	}

}
