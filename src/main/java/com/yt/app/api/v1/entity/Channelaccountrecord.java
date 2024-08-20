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
 * @version v1 @createdate2023-11-18 12:44:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channelaccountrecord extends YtBaseEntity<Channelaccountrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String channelname;
	Long userid;
	String ordernum;
	Integer type;
	String typename;
	Double pretotalincome;
	Double prewithdrawamount;
	Double pretowithdrawamount;
	Double pretoincomeamount;
	Double posttotalincome;
	Double postwithdrawamount;
	Double posttowithdrawamount;
	Double posttoincomeamount;
	String remark;
	Integer version;
}