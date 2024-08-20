package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
}
