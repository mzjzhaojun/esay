package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysSubmitDTO {

	String merchantid;
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
	String qrcode;
	String sign;

	public SysSubmitDTO() {
	}

	public SysSubmitDTO(String merchantid, String merchantorderid, Integer paytype, Double payamt, String notifyurl,
			String banknum, String bankcode, String bankowner, String bankaddress, String remark, String merchantip,
			String sign) {
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
