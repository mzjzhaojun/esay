package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-31 13:31:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Logs implements Serializable {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String optname;
	java.util.Date optdate;
	Integer type;
	String typename;
	String method;
	String requesturl;
	String requestparams;
	String requestip;
	Integer time;
}