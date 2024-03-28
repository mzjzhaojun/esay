package com.yt.app.common.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.google.common.collect.Lists;
import com.yt.app.api.v1.bo.JwtUserBO;
import com.yt.app.common.base.constant.SecurityConstant;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.enums.YtCodeEnum;
import com.yt.app.common.exption.MyException;
import com.yt.app.common.util.AuthUtil;
import com.yt.app.common.util.RedisUtil;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 拦截器 -- token用户信息
 * </p>
 *
 * @author zhengqingya
 * @description 注册使用参考 {@link WebAppConfig}
 * @date 2022/1/10 16:28
 */

public class HandlerInterceptorForToken implements HandlerInterceptor {

	private Pattern allowedMethods = Pattern.compile("^(HEAD|TRACE|OPTIONS)$");

	private AntPathRequestMatcher[] requestMatchers = { new AntPathRequestMatcher("/rest/v1/auth/**"),
			new AntPathRequestMatcher("/rest/v1/file/dt/**"), new AntPathRequestMatcher("/rest/v1/order/**"),
			new AntPathRequestMatcher("/app/v1/auth/**") };

	public HandlerInterceptorForToken(YtConfig config) {
	}

	/**
	 * 在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理
	 * {@link com.zhengqing.gateway.filter.AuthFilter#filter }
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 判断是否放行接口
		if (this.isOpenApi(request)) {
			return true;
		}

		// 校验权限
		JwtUserBO jwtUserBO = this.checkPermission(request);
		JwtUserContext.set(jwtUserBO);
		SysUserContext.setUserId(Long.valueOf(jwtUserBO.getUserId()));
		SysUserContext.setUsername(jwtUserBO.getUsername());
		return true;
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

	/**
	 * 校验权限
	 *
	 * @param request 请求
	 * @return 登录用户信息
	 * @author zhengqingya
	 * @date 2023/2/13 15:52
	 */
	private JwtUserBO checkPermission(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		if (StrUtil.isBlank(token)) {
			throw new MyException(YtCodeEnum.YT401.getDesc(), YtCodeEnum.YT401);
		}
		// 获取登录用户信息
		JwtUserBO jwtUserBO = AuthUtil.getLoginUser(token);
		String method = request.getMethod();
		String path = request.getRequestURI();
		// "GET:/web/api/user/*"
		String restfulPath = method + ":" + path;

		/**
		 * URL鉴权 [URL-角色集合] [{'key':'GET:/web/api/user/*','value':['ADMIN','TEST']},...]
		 */
		Map<Object, Object> urlPermReRoleMap = RedisUtil
				.hGetAll(SecurityConstant.URL_PERM_RE_ROLES + TenantIdContext.getTenantId());

		// 根据请求路径获取有访问权限的角色列表
		List<String> authorizedRoleList = Lists.newLinkedList();
		// 是否需要鉴权，默认未设置拦截规则不需鉴权
		boolean isCheck = false;
		PathMatcher pathMatcher = new AntPathMatcher();
		for (Map.Entry<Object, Object> permRoles : urlPermReRoleMap.entrySet()) {
			String perm = (String) permRoles.getKey();
			if (pathMatcher.match(perm, restfulPath)) {
				List<String> roleCodeList = JSONUtil.toList((String) permRoles.getValue(), String.class);
				authorizedRoleList.addAll(roleCodeList);
				isCheck = true;
			}
		}

		if (!isCheck) {
			return jwtUserBO;
		}

		if (CollectionUtil.isNotEmpty(authorizedRoleList)) {

			List<String> roleCodeList = jwtUserBO.getRoleCodeList();
			for (String roleCodeItem : roleCodeList) {
				if (authorizedRoleList.contains(roleCodeItem)) {
					return jwtUserBO;
				}
			}
		}
		throw new MyException(YtCodeEnum.YT401.getDesc(), YtCodeEnum.YT401);

	}

	/**
	 * 在业务处理器处理请求执行完成后，生成视图之前执行。
	 * 后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		SysUserContext.remove();
		JwtUserContext.remove();
	}

}
