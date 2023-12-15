package com.yt.app.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The SecurityConfiguration class provides a centralized location for
 * application security configuration. This class bootstraps the Spring Security
 * components during application startup.
 * 
 * @author ZJ
 */
@Configuration
@Order(-20)
public class YtSecurity extends WebSecurityConfigurerAdapter {

	/**
	 * This method builds the AuthenticationProvider used by the system to process
	 * authentication requests.
	 * 
	 * @param auth An AuthenticationManagerBuilder instance used to construct the
	 *             AuthenticationProvider.
	 * @throws Exception Thrown if a problem occurs constructing the
	 *                   AuthenticationProvider.
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	}

	/**
	 * This inner class configures the WebSecurityConfigurerAdapter instance for the
	 * web service API context paths.
	 * 
	 * @author ZJ
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/**").permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**");
	}

}
