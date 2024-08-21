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
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Incomemerchantaccountrecord extends YtBaseEntity<Incomemerchantaccountrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String merchantname;
	String ordernum;
	Integer type;
	Object pretotalincome;
	Object pretoincomeamount;
	Object prewithdrawamount;
	Object pretowithdrawamount;
	Object posttotalincome;
	Object posttoincomeamount;
	Object postwithdrawamount;
	Object posttowithdrawamount;
	String remark;
	Integer version;
}