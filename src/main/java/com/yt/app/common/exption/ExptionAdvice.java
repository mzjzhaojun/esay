package com.yt.app.common.exption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.enums.YtCodeEnum;

import cn.dev33.satoken.exception.NotLoginException;

/**
 * zj
 */
@RestControllerAdvice
public class ExptionAdvice {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(RuntimeException.class)
	public YtBody handleRuntimeException(RuntimeException e) {
		if (e instanceof HttpMessageConversionException) {
			logger.error("HttpMessageConversionException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof NullPointerException) {
			logger.error("NullPointerException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT202.getCode());
		} else if (e instanceof ClassCastException) {
			logger.error("ClassCastException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT203.getCode());
		} else if (e instanceof IllegalArgumentException) {
			logger.error("IllegalArgumentException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT204.getCode());
		} else if (e instanceof ArithmeticException) {
			logger.error("ArithmeticException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT205.getCode());
		} else if (e instanceof ArrayStoreException) {
			logger.error("ArrayStoreException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT206.getCode());
		} else if (e instanceof IndexOutOfBoundsException) {
			logger.error("IndexOutOfBoundsException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT207.getCode());
		} else if (e instanceof NegativeArraySizeException) {
			logger.error("NegativeArraySizeException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT208.getCode());
		} else if (e instanceof NumberFormatException) {
			logger.error("NumberFormatException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT209.getCode());
		} else if (e instanceof NegativeArraySizeException) {
			logger.error("SecurityException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT210.getCode());
		} else if (e instanceof NegativeArraySizeException) {
			logger.error("UnsupportedOperationException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT211.getCode());
		} else if (e instanceof MyException) {
			logger.error("Ytan :", e.getMessage(), e);
			return new YtBody(e.getMessage(), ((MyException) e).getCode().getCode());
		} else if (e instanceof NotLoginException) {
			logger.error("NotLoginException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT303.getCode());
		}
		logger.error("RuntimeException:", e.getMessage(), e);
		return new YtBody("RuntimeException" + e.getMessage(), YtCodeEnum.YT500.getCode());
	}

	@ExceptionHandler(Exception.class)
	public YtBody handleException(Exception e) {
		if (e instanceof HttpRequestMethodNotSupportedException) {
			logger.error("HttpRequestMethodNotSupportedException:" + e.getMessage(), e);
			return new YtBody("HttpRequestMethodNotSupportedException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpMediaTypeNotSupportedException) {
			logger.error("HttpMediaTypeNotSupportedException:" + e.getMessage(), e);
			return new YtBody("HttpMediaTypeNotSupportedException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpMediaTypeException) {
			logger.error("HttpMediaTypeException:" + e.getMessage(), e);
			return new YtBody("HttpMediaTypeException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpMediaTypeNotAcceptableException) {
			logger.error("HttpMediaTypeNotAcceptableException:" + e.getMessage(), e);
			return new YtBody("HttpMediaTypeNotAcceptableException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpSessionRequiredException) {
			logger.error("HttpSessionRequiredException:" + e.getMessage(), e);
			return new YtBody("HttpSessionRequiredException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		}

		logger.error("Exception:" + e.getMessage(), e);
		return new YtBody("Exception:" + e.getMessage(), YtCodeEnum.YT500.getCode());
	}
}