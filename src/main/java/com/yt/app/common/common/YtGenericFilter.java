package com.yt.app.common.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.yt.app.common.config.YtConfig;

@Profile("dev")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class YtGenericFilter extends GenericFilterBean {

	@Autowired
	YtConfig ytconfig;

	@Override
	public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain) throws IOException, ServletException {
		HttpServletResponse localHttpServletResponse = (HttpServletResponse) paramServletResponse;
		localHttpServletResponse.setHeader("Access-Control-Allow-Origin", this.ytconfig.getOrigin());
		localHttpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
		localHttpServletResponse.setHeader("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, POST, PUT");
		localHttpServletResponse.setHeader("Access-Control-Max-Age", "3600");
		localHttpServletResponse.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Tenant_Id,Tenant_Flag,Pagenum,Pagesize,OrderBy,Dir,Satoken,Signa,Authorization");
		localHttpServletResponse.setHeader("Access-Control-Expose-Headers", "Signa,Sign,Authorization");
		localHttpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");
		localHttpServletResponse.setHeader("Content-Security-Policy", "script-src * 'unsafe-inline' 'unsafe-eval'");
		localHttpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
		localHttpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
		paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
	}
}
