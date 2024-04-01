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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Configuration
public class YtWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

	@Autowired
	YtConfig g;

	@Bean(name = "YtMappingJackson2HttpMessageConverter")
	public YtJackson2HttpMessageConverter t() {
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		SimpleModule localSimpleModule = new SimpleModule();
		localSimpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		localSimpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		localSimpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
		localObjectMapper.registerModule(localSimpleModule);
		localObjectMapper.setDateFormat(new SimpleDateFormat("YY-MM-dd"));
		localObjectMapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		YtJackson2HttpMessageConverter localMappingJackson2HttpMessageConverter = new YtJackson2HttpMessageConverter(
				localObjectMapper);
		return localMappingJackson2HttpMessageConverter;
	}

	@Bean(name = "YtAbstractMessageConverterMethodProcessor")
	public YtMessageConverterMethodProcessor n() {
		List<HttpMessageConverter<?>> paramList = new ArrayList<HttpMessageConverter<?>>();
		paramList.add(t());
		YtMessageConverterMethodProcessor localythttpentitymethodprocessor = new YtMessageConverterMethodProcessor(
				paramList);
		return localythttpentitymethodprocessor;
	}

	@Bean(name = "YtDecryptMappingJackson2HttpMessageConverter")
	public YtDecryptJackson2HttpMessageConverter k() {
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		SimpleModule localSimpleModule = new SimpleModule();
		localSimpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		localSimpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		localSimpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
		localObjectMapper.setDateFormat(new SimpleDateFormat("YY-MM-dd"));
		localObjectMapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));

		localObjectMapper.registerModule(localSimpleModule);
		YtDecryptJackson2HttpMessageConverter localMappingJackson2HttpMessageConverter = new YtDecryptJackson2HttpMessageConverter(
				localObjectMapper, g);
		return localMappingJackson2HttpMessageConverter;
	}

	@Bean(name = "YtDecryptAbstractMessageConverterMethodProcessor")
	public YtDecryptMessageConverterMethodProcessor m() {
		List<HttpMessageConverter<?>> paramList = new ArrayList<HttpMessageConverter<?>>();
		paramList.add(k());
		YtDecryptMessageConverterMethodProcessor localrequestdecryptresponseencryptbodymethodprocessor = new YtDecryptMessageConverterMethodProcessor(
				paramList);
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

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry paramResourceHandlerRegistry) {
//		paramResourceHandlerRegistry.addResourceHandler(new String[] { "/**" })
//				.addResourceLocations(new String[] { "classpath:/template/" });
//		paramResourceHandlerRegistry.addResourceHandler("/").addResourceLocations("classpath:/index.html");
//		super.addResourceHandlers(paramResourceHandlerRegistry);
//	}

//	@Bean(name = "InternalResourceViewResolver")
//	public InternalResourceViewResolver l() {
//		InternalResourceViewResolver localInternalResourceViewResolver = new InternalResourceViewResolver();
//		localInternalResourceViewResolver.setPrefix("/");
//		localInternalResourceViewResolver.setSuffix(".html");
//		return localInternalResourceViewResolver;
//	}

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("forward:/static/index.html");
//		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		super.addViewControllers(registry);
//	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 可添加多个
		registry.addInterceptor(new HandlerInterceptorForToken(g)).addPathPatterns("/**");
		registry.addInterceptor(new HandlerInterceptorForTenantId(g)).addPathPatterns("/**");
		registry.addInterceptor(new HandlerInterceptorForRsaKey(g)).addPathPatterns("/**");
	}
}