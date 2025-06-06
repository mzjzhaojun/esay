package com.yt.app.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.BaseVO;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MerchantaccountorderVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String username;
	String qrcodeaislecode;
	Long qrcodeaisleid;
	String qrcodeaislename;
	String qrcodename;
	Long qrcodeid;
	String qrcodecode;
	String ordernum;
	String type;
	Boolean dynamic;
	String typename;
	Double fewamount;
	String merchantcode;
	Double amount;
	Double collection;
	Double realamount;
	String resulturl;
	String qrocde;
	Integer status;
	String statusname;
	String remark;
	String imgurl;
	String accnumber;
	Double exchange;
	Double merchantexchange;
	Double amountreceived;
	Double usdtval;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Double incomeamount;
	Integer version;

	// 订单总数
	Integer ordercount;
}