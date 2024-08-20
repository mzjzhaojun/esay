package com.yt.app.api.v1.entity;

import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 20:07:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Systemaccountrecord extends YtBaseEntity<Systemaccountrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long systemaccountid;
	String name;
	Integer type;
	String typename;
	Double pretotalincome;
	Double prewithdrawamount;
	Double posttotalincome;
	Double postwithdrawamount;
	Double amount;
	Double balance;

	Double usdtpretotalincome;
	Double usdtprewithdrawamount;
	Double usdtposttotalincome;
	Double usdtpostwithdrawamount;
	Double usdtamount;
	Double usdtbalance;

	String remark;
	Integer version;

}