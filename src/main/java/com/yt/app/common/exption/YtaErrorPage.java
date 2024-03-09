package com.yt.app.common.exption;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Component
public class YtaErrorPage implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		ErrorPage[] errorPages = new ErrorPage[3];
		errorPages[0] = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401");
		errorPages[1] = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
		errorPages[2] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
		registry.addErrorPages(errorPages);
	}
}