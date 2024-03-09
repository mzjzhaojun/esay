package com.yt.app.common.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import com.yt.app.common.common.yt.YtHttpEntity;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

public class YtMessageConverterMethodProcessor extends AbstractMessageConverterMethodProcessor {

	public YtMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters) {
		super(converters);
	}

	public YtMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager) {

		super(converters, manager);
	}

	public YtMessageConverterMethodProcessor(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {

		super(converters, null, requestResponseBodyAdvice);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return YtRequestEntity.class == parameter.getParameterType();
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return YtResponseEntity.class == returnType.getParameterType();
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
			throws IOException, HttpMediaTypeNotSupportedException {
		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		Type paramType = getHttpEntityType(parameter);
		Object body = readWithMessageConverters(webRequest, parameter, paramType);
		if (YtRequestEntity.class == parameter.getParameterType()) {
			return new YtRequestEntity<Object>(body, inputMessage.getHeaders(), inputMessage.getMethod(),
					inputMessage.getURI());
		} else {
			return new YtHttpEntity<Object>(body, inputMessage.getHeaders());
		}
	}

	private Type getHttpEntityType(MethodParameter parameter) {
		Assert.isAssignable(YtHttpEntity.class, parameter.getParameterType());
		Type parameterType = parameter.getGenericParameterType();
		if (parameterType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) parameterType;
			if (type.getActualTypeArguments().length != 1) {
				throw new IllegalArgumentException("Expected single generic parameter on '"
						+ parameter.getParameterName() + "' in method " + parameter.getMethod());
			}
			return type.getActualTypeArguments()[0];
		} else if (parameterType instanceof Class) {
			return Object.class;
		}
		throw new IllegalArgumentException("HttpEntity parameter '" + parameter.getParameterName() + "' in method "
				+ parameter.getMethod() + " is not parameterized");
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
		if (returnValue == null) {
			return;
		}
		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
		Assert.isInstanceOf(YtHttpEntity.class, returnValue);
		YtHttpEntity<?> responseEntity = (YtHttpEntity<?>) returnValue;
		HttpHeaders entityHeaders = responseEntity.getHeaders();
		if (!entityHeaders.isEmpty()) {
			outputMessage.getHeaders().putAll(entityHeaders);
		}
		Object body = responseEntity.getBody();
		if (responseEntity instanceof YtResponseEntity) {
			outputMessage.setStatusCode(((YtResponseEntity<?>) responseEntity).getStatusCode());
			if (HttpMethod.GET == inputMessage.getMethod() && isResourceNotModified(inputMessage, outputMessage)) {
				outputMessage.setStatusCode(HttpStatus.NOT_MODIFIED);
				outputMessage.flush();
				return;
			}
		}
		writeWithMessageConverters(body, returnType, inputMessage, outputMessage);
		outputMessage.flush();
	}

	private boolean isResourceNotModified(ServletServerHttpRequest inputMessage,
			ServletServerHttpResponse outputMessage) {
		List<String> ifNoneMatch = inputMessage.getHeaders().getIfNoneMatch();
		long ifModifiedSince = inputMessage.getHeaders().getIfModifiedSince();
		String eTag = addEtagPadding(outputMessage.getHeaders().getETag());
		long lastModified = outputMessage.getHeaders().getLastModified();
		boolean notModified = false;

		if (!ifNoneMatch.isEmpty() && (inputMessage.getHeaders().containsKey(HttpHeaders.IF_UNMODIFIED_SINCE)
				|| inputMessage.getHeaders().containsKey(HttpHeaders.IF_MATCH))) {
		} else if (lastModified != -1 && StringUtils.hasLength(eTag)) {
			notModified = isETagNotModified(ifNoneMatch, eTag) && isTimeStampNotModified(ifModifiedSince, lastModified);
		} else if (lastModified != -1) {
			notModified = isTimeStampNotModified(ifModifiedSince, lastModified);
		} else if (StringUtils.hasLength(eTag)) {
			notModified = isETagNotModified(ifNoneMatch, eTag);
		}
		return notModified;
	}

	private boolean isETagNotModified(List<String> ifNoneMatch, String etag) {
		if (StringUtils.hasLength(etag)) {
			for (String clientETag : ifNoneMatch) {
				if (StringUtils.hasLength(clientETag)
						&& (clientETag.replaceFirst("^W/", "").equals(etag.replaceFirst("^W/", "")))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isTimeStampNotModified(long ifModifiedSince, long lastModifiedTimestamp) {
		return (ifModifiedSince >= (lastModifiedTimestamp / 1000 * 1000));
	}

	private String addEtagPadding(String etag) {
		if (StringUtils.hasLength(etag)
				&& (!(etag.startsWith("\"") || etag.startsWith("W/\"")) || !etag.endsWith("\""))) {
			etag = "\"" + etag + "\"";
		}
		return etag;
	}

	@Override
	protected Class<?> getReturnValueType(Object returnValue, MethodParameter returnType) {
		if (returnValue != null) {
			return returnValue.getClass();
		} else {
			Type type = getHttpEntityType(returnType);
			return ResolvableType.forMethodParameter(returnType, type).resolve(Object.class);
		}
	}

}
