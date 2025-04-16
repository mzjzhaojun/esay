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
 * @version v1 @createdate2025-04-16 23:44:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Qrcodpaymember extends YtBaseEntity<Qrcodpaymember> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long qrcodeid;
	String qrcodename;
	String mobile;
	String name;
	String pcardno;
	String cardno;
	String smsno;
	String protocolno;
	Integer status;
	String memberid;
	String token;
	String remark;
	Integer version;
}