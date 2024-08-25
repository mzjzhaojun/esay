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
public class IncomemerchantaccountorderVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String merchantname;
	String merchantcode;
	Long qrcodeaisleid;
	String qrcodeaislename;
	String qrcodename;
	Long qrcodeid;
	String qrcodecode;
	String ordernum;
	String type;
	String typename;
	Double fewamount;
	java.util.Date expireddate;
	Double amount;
	Double realamount;
	String resulturl;
	String qrocde;
	Integer status;
	String statusname;
	String remark;
	String imgurl;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Double incomeamount;
	Integer version;
}