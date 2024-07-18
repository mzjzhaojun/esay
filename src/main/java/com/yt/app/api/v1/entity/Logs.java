package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-31 13:31:43
 */
@Getter
@Setter
public class Logs implements Serializable {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String optname;
	java.util.Date optdate;
	Integer type;
	String method;
	String requesturl;
	String requestparams;
	String requestip;
	Integer time;

	public Logs() {
	}

	public Logs(String optname, java.util.Date optdate, String ip, Integer type) {
		this.optname = optname;
		this.optdate = optdate;
		this.requestip = ip;
		this.type = type;
	}

	public Logs(Long id, String optname, java.util.Date optdate, Integer type, String method, String requesturl,
			String requestparams, String requestip, Integer time) {
		this.id = id;
		this.optname = optname;
		this.optdate = optdate;
		this.type = type;
		this.method = method;
		this.requesturl = requesturl;
		this.requestparams = requestparams;
		this.requestip = requestip;
		this.time = time;
	}
}