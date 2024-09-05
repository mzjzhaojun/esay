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
 * @version v1 @createdate2024-08-23 23:31:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Incomemerchantaccountorder extends YtBaseEntity<Incomemerchantaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String merchantname;
	String qrcodeaislecode;
	Long qrcodeaisleid;
	String qrcodeaislename;
	String qrcodename;
	Long qrcodeid;
	String qrcodecode;
	String ordernum;
	String type;
	Boolean dynamic;
	Double fewamount;
	java.util.Date expireddate;
	Double amount;
	Double realamount;
	String resulturl;
	String qrocde;
	Integer status;
	String remark;
	Long merchantid;
	Double incomeamount;
	Integer version;
}