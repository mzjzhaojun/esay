package com.yt.app.common.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Component
public class YtServletContextListener implements ServletContextListener {

	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>(
				requestMappingHandlerAdapter.getReturnValueHandlers());
		int decryptIndex = 0;
		int encryptIndex = 0;
		int requestIndex = 0;
		int responseIndex = 0;
		YtDecryptMessageConverterMethodProcessor requestDecryptResponseEncryptBodyMethodProcessor = null;
		YtMessageConverterMethodProcessor ythttpentitymethodprocessor = null;
		for (int i = 0, length = handlers.size(); i < length; i++) {
			HandlerMethodReturnValueHandler handler = handlers.get(i);
			if (handler instanceof YtDecryptMessageConverterMethodProcessor) {
				requestDecryptResponseEncryptBodyMethodProcessor = (YtDecryptMessageConverterMethodProcessor) handler;
				encryptIndex = i;
			} else if (handler instanceof HttpEntityMethodProcessor) {
				decryptIndex = i;
			}

			if (handler instanceof YtMessageConverterMethodProcessor) {
				ythttpentitymethodprocessor = (YtMessageConverterMethodProcessor) handler;
				requestIndex = i;
			} else if (handler instanceof HttpEntityMethodProcessor) {
				responseIndex = i;
			}
		}
		if (requestDecryptResponseEncryptBodyMethodProcessor != null) {
			handlers.remove(encryptIndex);
			handlers.add(decryptIndex - 1, requestDecryptResponseEncryptBodyMethodProcessor);
		}
		if (ythttpentitymethodprocessor != null) {
			handlers.remove(requestIndex);
			handlers.add(responseIndex - 1, ythttpentitymethodprocessor);
		}
		requestMappingHandlerAdapter.setReturnValueHandlers(handlers);

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(
				requestMappingHandlerAdapter.getArgumentResolvers());
		YtDecryptMessageConverterMethodProcessor requestDecryptResponseEncryptBodyMethodProcessor2 = null;
		for (int i = 0, length = argumentResolvers.size(); i < length; i++) {
			HandlerMethodArgumentResolver argumentResolver = argumentResolvers.get(i);
			if (argumentResolver instanceof YtDecryptMessageConverterMethodProcessor) {
				requestDecryptResponseEncryptBodyMethodProcessor2 = (YtDecryptMessageConverterMethodProcessor) argumentResolver;
				encryptIndex = i;
			} else if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
				decryptIndex = i;
			}

			if (argumentResolver instanceof YtMessageConverterMethodProcessor) {
				ythttpentitymethodprocessor = (YtMessageConverterMethodProcessor) argumentResolver;
				requestIndex = i;
			} else if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
				responseIndex = i;
			}
		}
		if (requestDecryptResponseEncryptBodyMethodProcessor2 != null) {
			argumentResolvers.remove(encryptIndex);
			argumentResolvers.add(decryptIndex - 1, requestDecryptResponseEncryptBodyMethodProcessor2);
		}
		if (ythttpentitymethodprocessor != null) {
			argumentResolvers.remove(requestIndex);
			argumentResolvers.add(responseIndex - 1, ythttpentitymethodprocessor);
		}
		requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}

}
