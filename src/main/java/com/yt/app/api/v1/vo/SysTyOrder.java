package com.yt.app.api.v1.vo;

import com.yt.app.api.v1.dbo.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysTyOrder extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer merchant_id;
	String typay_order_id;
	String merchant_order_id;
	Integer pay_type;
	Double pay_amt;
	Integer pay_message;
	String pay_result;
	String bank_code;
	String remark;
	String payment_time;
	String bank_account;
	Integer order_status;
	String member_account;
	String sign;
	String image_address;

}
