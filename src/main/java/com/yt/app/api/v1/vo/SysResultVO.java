package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysResultVO {

	String merchantid;
	String payorderid;
	String merchantorderid;
	Integer paytype;
	Double payamt;
	String bankcode;
	Integer code;
	String remark;
	String sign;
	Double balance;

	public SysResultVO() {
	}

	public SysResultVO(String merchantid, String payorderid, String merchantorderid, Integer paytype, Double payamt,
			String bankcode, String remark, String sign) {
		super();
		this.merchantid = merchantid;
		this.payorderid = payorderid;
		this.merchantorderid = merchantorderid;
		this.paytype = paytype;
		this.payamt = payamt;
		this.bankcode = bankcode;
		this.remark = remark;
		this.sign = sign;
	}

}
