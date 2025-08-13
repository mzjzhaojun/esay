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
 * @version v1 @createdate2025-08-12 22:27:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Betting extends YtBaseEntity<Betting> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String wagerstype;
	String tid;
	String teamc;
	String teamh;
	String site;
	String showtextordertypeioratio;
	String showtextleague;
	String ordertype;
	String numc;
	String numh;
	String name;
	String gt;
	String gold;
	String aresult;
	String inradio;
	String remark;
	Integer version;
}