package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaySubmitDTO {

	String merchantid;
	String merchantorderid;
	String payaisle;
	Double payamount;
	String notifyurl;
	String banknum;
	String bankcode;
	String bankname;
	String bankowner;
	String bankaddress;
	String sign;
}
