package com.yt.app.common.exption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.enums.YtCodeEnum;

@RestController
@RequestMapping("/error")
public class Ytab {

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/401")
	public ModelAndView errorHtml404(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		return new ModelAndView("static/error/401");
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/404")
	public ModelAndView errorHtml401(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		return new ModelAndView("static/error/404");
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE, value = "/500")
	public ModelAndView errorHtml500(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		return new ModelAndView("static/error/500");
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/401")
	public YtResponseEntity<Object> error404(HttpServletRequest request) {
		return new YtResponseEntity<Object>(new YtBody(YtCodeEnum.YT300));
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/404")
	public YtResponseEntity<Object> error401(HttpServletRequest request) {
		return new YtResponseEntity<Object>(new YtBody(YtCodeEnum.YT400));
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/500")
	public YtResponseEntity<Object> error500(HttpServletRequest request) {
		Object obj = request.getAttribute("code");
		if (obj instanceof YtCodeEnum) {
			YtCodeEnum code = (YtCodeEnum) obj;
			return new YtResponseEntity<Object>(new YtBody(code));
		}
		return new YtResponseEntity<Object>(new YtBody(YtCodeEnum.YT500));
	}
}
