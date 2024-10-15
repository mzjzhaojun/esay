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
 * @version v1 @createdate2024-10-15 00:23:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tronmemberorder extends YtBaseEntity<Tronmemberorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long chatid;
	Integer messageid;
	Long tgid;
	String ordernum;
	Integer type;
	String goodsname;
	String fromaddress;
	Double amount;
	Double realamount;
	Integer trxamount;
	Double usdtamount;
	Double fewamount;
	String incomeaddress;
	Integer status;
	String remark;
	java.util.Date successdate;
	java.util.Date expireddate;
	Integer version;
}