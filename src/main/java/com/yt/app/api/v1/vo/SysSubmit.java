package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysSubmit {

	Integer merchantid;
	String merchantorderid;
	Integer paytype;
	Double payamt;
	String notifyurl;
	String banknum;
	String bankcode;
	String bankname;
	String bankowner;
	String bankaddress;
	String remark;
	String merchantip;
	String sign;


	public SysSubmit() {
	}


	public SysSubmit(Integer merchantid, String merchantorderid, Integer paytype, Double payamt,
			String notifyurl, String banknum, String bankcode, String bankowner, String bankaddress, String remark,
			String merchantip, String sign) {
		super();
		this.merchantid = merchantid;
		this.merchantorderid = merchantorderid;
		this.paytype = paytype;
		this.payamt = payamt;
		this.notifyurl = notifyurl;
		this.banknum = banknum;
		this.bankcode = bankcode;
		this.bankowner = bankowner;
		this.bankaddress = bankaddress;
		this.remark = remark;
		this.merchantip = merchantip;
		this.sign = sign;
	}

	

}
