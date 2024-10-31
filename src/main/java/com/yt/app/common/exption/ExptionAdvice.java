package com.yt.app.common.exption;

import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.enums.YtCodeEnum;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;

/**
 * zj
 */
@Slf4j
@RestControllerAdvice
public class ExptionAdvice {

	@ExceptionHandler(RuntimeException.class)
	public YtBody handleRuntimeException(RuntimeException e) {
		if (e instanceof HttpMessageConversionException) {
			log.info("HttpMessageConversionException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof NotLoginException) {
			log.info("NotLoginException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT401.getCode());
		} else if (e instanceof YtException) {
			log.info("MyException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), ((YtException) e).getCode().getCode());
		}
		log.info("msg:", e.getMessage(), e);
		return new YtBody(e.getMessage(), YtCodeEnum.YT400.getCode());
	}

	@ExceptionHandler(Exception.class)
	public YtBody handleException(Exception e) {
		log.info("Exception:" + e.getMessage(), e);
		return new YtBody("Exception:" + e.getMessage(), YtCodeEnum.YT500.getCode());
	}
}