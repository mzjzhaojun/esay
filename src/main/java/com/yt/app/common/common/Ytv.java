package com.yt.app.common.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.security.HandlerInterceptorForRsaKey;
import com.yt.app.common.security.HandlerInterceptorForTenantId;
import com.yt.app.common.security.HandlerInterceptorForToken;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Configuration
public class Ytv extends WebMvcConfigurationSupport {

	@Autowired
	YtConfig g;

	@Bean(name = "YtMappingJackson2HttpMessageConverter")
	public Ytq t() {
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		SimpleModule localSimpleModule = new SimpleModule();
		localSimpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		localSimpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		localSimpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
		localObjectMapper.registerModule(localSimpleModule);
		localObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		localObjectMapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		Ytq localMappingJackson2HttpMessageConverter = new Ytq(localObjectMapper);
		return localMappingJackson2HttpMessageConverter;
	}

	@Bean(name = "YtAbstractMessageConverterMethodProcessor")
	public Ytd n() {
		List<HttpMessageConverter<?>> paramList = new ArrayList<HttpMessageConverter<?>>();
		paramList.add(t());
		Ytd localythttpentitymethodprocessor = new Ytd(paramList);
		return localythttpentitymethodprocessor;
	}

	@Bean(name = "YtDecryptMappingJackson2HttpMessageConverter")
	public Yto k() {
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		SimpleModule localSimpleModule = new SimpleModule();
		localSimpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		localSimpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		localSimpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
		localObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		localObjectMapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));

		localObjectMapper.registerModule(localSimpleModule);
		Yto localMappingJackson2HttpMessageConverter = new Yto(localObjectMapper, g);
		return localMappingJackson2HttpMessageConverter;
	}

	@Bean(name = "YtDecryptAbstractMessageConverterMethodProcessor")
	public Ytn m() {
		List<HttpMessageConverter<?>> paramList = new ArrayList<HttpMessageConverter<?>>();
		paramList.add(k());
		Ytn localrequestdecryptresponseencryptbodymethodprocessor = new Ytn(paramList);
		return localrequestdecryptresponseencryptbodymethodprocessor;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		super.addArgumentResolvers(argumentResolvers);
		argumentResolvers.add(n());
		argumentResolvers.add(m());
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		super.addReturnValueHandlers(returnValueHandlers);
		returnValueHandlers.add(n());
		returnValueHandlers.add(m());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry paramResourceHandlerRegistry) {
		paramResourceHandlerRegistry.addResourceHandler(new String[] { "/**" })
				.addResourceLocations(new String[] { "classpath:/template/" });
		// paramResourceHandlerRegistry.addResourceHandler("/**").addResourceLocations("classpath:/assets/");
		paramResourceHandlerRegistry.addResourceHandler("/").addResourceLocations("classpath:/index.html");
		super.addResourceHandlers(paramResourceHandlerRegistry);
	}

	@Bean(name = "InternalResourceViewResolver")
	public InternalResourceViewResolver l() {
		InternalResourceViewResolver localInternalResourceViewResolver = new InternalResourceViewResolver();
		localInternalResourceViewResolver.setPrefix("/");
		localInternalResourceViewResolver.setSuffix(".html");
		return localInternalResourceViewResolver;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/static/index.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 可添加多个
		registry.addInterceptor(new HandlerInterceptorForToken(g)).addPathPatterns("/rest/**");
		registry.addInterceptor(new HandlerInterceptorForTenantId(g)).addPathPatterns("/rest/**");
		registry.addInterceptor(new HandlerInterceptorForRsaKey(g)).addPathPatterns("/rest/**");
	}
}