package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-12 10:55:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channel extends YtBaseEntity<Channel> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String name;
	String nkname;
	String code;
	Boolean status;
	Double balance;
	Double remotebalance;
	Double exchange;
	Double onecost;
	Double exchangeonecost;
	Double exchangedownpoint;
	Double downpoint;
	Double costexchange;
	Integer min;
	Integer max;
	Integer weight;
	Integer mtorder;
	Boolean ifordernum;
	Boolean firstmatch;
	String firstmatchmoney;
	String apiip;
	String apikey;
	String apireusultip;
	String remark;
	Integer version;
}