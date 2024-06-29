package com.yt.app.common.exption;

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
			log.error("HttpMessageConversionException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof NullPointerException) {
			log.error("NullPointerException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof ClassCastException) {
			log.error("ClassCastException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT888.getCode());
		} else if (e instanceof IllegalArgumentException) {
			log.error("IllegalArgumentException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT888.getCode());
		} else if (e instanceof ArithmeticException) {
			log.error("ArithmeticException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT888.getCode());
		} else if (e instanceof ArrayStoreException) {
			log.error("ArrayStoreException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT206.getCode());
		} else if (e instanceof IndexOutOfBoundsException) {
			log.error("IndexOutOfBoundsException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT207.getCode());
		} else if (e instanceof NegativeArraySizeException) {
			log.error("NegativeArraySizeException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT208.getCode());
		} else if (e instanceof NumberFormatException) {
			log.error("NumberFormatException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT209.getCode());
		} else if (e instanceof NegativeArraySizeException) {
			log.error("SecurityException:", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT210.getCode());
		} else if (e instanceof NegativeArraySizeException) {
			log.error("UnsupportedOperationException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT211.getCode());
		} else if (e instanceof MyException) {
			log.error("Ytan :", e.getMessage(), e);
			return new YtBody(e.getMessage(), ((MyException) e).getCode().getCode());
		} else if (e instanceof NotLoginException) {
			log.error("NotLoginException :", e.getMessage(), e);
			return new YtBody(e.getMessage(), YtCodeEnum.YT401.getCode());
		}
		log.error("RuntimeException:", e.getMessage(), e);
		return new YtBody("RuntimeException" + e.getMessage(), YtCodeEnum.YT500.getCode());
	}

	@ExceptionHandler(Exception.class)
	public YtBody handleException(Exception e) {
		if (e instanceof HttpRequestMethodNotSupportedException) {
			log.error("HttpRequestMethodNotSupportedException:" + e.getMessage(), e);
			return new YtBody("HttpRequestMethodNotSupportedException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpMediaTypeNotSupportedException) {
			log.error("HttpMediaTypeNotSupportedException:" + e.getMessage(), e);
			return new YtBody("HttpMediaTypeNotSupportedException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpMediaTypeException) {
			log.error("HttpMediaTypeException:" + e.getMessage(), e);
			return new YtBody("HttpMediaTypeException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpMediaTypeNotAcceptableException) {
			log.error("HttpMediaTypeNotAcceptableException:" + e.getMessage(), e);
			return new YtBody("HttpMediaTypeNotAcceptableException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		} else if (e instanceof HttpSessionRequiredException) {
			log.error("HttpSessionRequiredException:" + e.getMessage(), e);
			return new YtBody("HttpSessionRequiredException :" + e.getMessage(), YtCodeEnum.YT500.getCode());
		}

		log.error("Exception:" + e.getMessage(), e);
		return new YtBody("Exception:" + e.getMessage(), YtCodeEnum.YT500.getCode());
	}
}