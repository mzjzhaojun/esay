package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Crownagent extends YtBaseEntity<Crownagent> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String name;
	String nikname;
	String code;
	Boolean status;
	Boolean synchronous;
	String type;
	String channelname;
	Long channelid;
	String cookie;
	String username;
	String password;
	String email;
	String domian;
	String uid;
	String ver;
	String selmaxid;
	String origin;
	String remark;
	Integer version;
}