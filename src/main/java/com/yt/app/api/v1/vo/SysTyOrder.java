package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysTyOrder {

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

	public SysTyOrder() {
	}

	public SysTyOrder(Integer merchant_id, String typay_order_id, String merchant_order_id, Integer pay_type,
			Double pay_amt, Integer pay_message, String pay_result, String bank_code, String remark,
			String payment_time, String bank_account, Integer order_status, String member_account, String sign,
			String image_address) {
		super();
		this.merchant_id = merchant_id;
		this.typay_order_id = typay_order_id;
		this.merchant_order_id = merchant_order_id;
		this.pay_type = pay_type;
		this.pay_amt = pay_amt;
		this.pay_message = pay_message;
		this.pay_result = pay_result;
		this.bank_code = bank_code;
		this.remark = remark;
		this.payment_time = payment_time;
		this.bank_account = bank_account;
		this.order_status = order_status;
		this.member_account = member_account;
		this.sign = sign;
		this.image_address = image_address;
	}

}
