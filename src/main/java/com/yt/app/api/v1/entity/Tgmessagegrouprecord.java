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
 * @version v1 @createdate2025-09-19 01:40:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tgmessagegrouprecord extends YtBaseEntity<Tgmessagegrouprecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long mid;
	Long cid;
	Long chatid;
	String mmanger;
	String cmanger;
	Integer mreplyid;
	Integer creplyid;
	String name;
	String qrcode;
	Double exchange;
	Double amount;
	Double usd;
	Boolean status;
	String optionname;
	String confirmname;
	String remark;
	Integer version;
}