package com.yt.app.common.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.config.YtConfig;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 拦截器 -- 租户ID
 * </p>
 *
 * @author zhengqingya
 * @description 注册使用参考 {@link WebAppConfig}
 * @date 2022/1/10 16:28
 */
public class HandlerInterceptorForTenantId implements HandlerInterceptor {

	public HandlerInterceptorForTenantId(YtConfig config) {
	}

	private Pattern allowedMethods = Pattern.compile("^(HEAD|TRACE|OPTIONS)$");

	private AntPathRequestMatcher[] requestMatchers = { new AntPathRequestMatcher("/app/v1/auth/**") };

	/**
	 * 租户ID字段名称
	 */
	private static final String TENANT_ID = "Tenant_Id";
	/**
	 * 是否排除租户ID标识
	 */
	private static final String TENANT_ID_FLAG = "Tenant_Flag";

	/**
	 * 在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理；
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String tenantId = request.getHeader(TENANT_ID);
		if (StringUtils.isNotBlank(tenantId)) {
			if (StringUtils.isNumeric(tenantId)) {
				// > 0
				TenantIdContext.setTenantId(Long.valueOf(tenantId));
			} else if ("-1".equals(tenantId)) {
				// 全部租户
				TenantIdContext.removeFlag();
			}
		}
		// 判断是否放行接口
		if (this.isOpenApi(request)) {
			TenantIdContext.removeFlag();
		}

		// 是否排除租户ID标识
		String tenantIdFlag = request.getHeader(TENANT_ID_FLAG);
		String isTenantIdFlag = "true";
		if (StringUtils.isNotBlank(tenantIdFlag) && isTenantIdFlag.equals(tenantIdFlag)) {
			TenantIdContext.removeFlag();
		}
		return true;
	}

	/**
	 * 在业务处理器处理请求执行完成后，生成视图之前执行。
	 * 后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		TenantIdContext.remove();
	}

	/**
	 * 判断是否放行接口
	 *
	 * @param request 请求
	 * @return void
	 * @author zhengqingya
	 * @date 2023/2/13 15:52
	 */
	private boolean isOpenApi(HttpServletRequest request) {
		boolean isOpen = false;
		if (this.matches(request)) {
			TenantIdContext.removeFlag();
			return true;
		}
		return isOpen;
	}

	public boolean matches(HttpServletRequest request) {
		for (AntPathRequestMatcher rm : requestMatchers) {
			if (rm.matches(request)) {
				return true;
			}
		}
		return allowedMethods.matcher(request.getMethod()).matches();
	}

}
