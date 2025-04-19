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
 * @version v1 @createdate2025-04-13 22:38:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Qrcodetransferrecord extends YtBaseEntity<Qrcodetransferrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long qrcodeid;
	String qrcodename;
	String outbizno;
	String ordernum;
	Double amount;
	String payeeid;
	String bankcode;
	String bankname;
	String payeename;
	String payeetype;
	Integer status;
	String fileid;
	String dowloadurl;
	String remark;
	Integer version;
}