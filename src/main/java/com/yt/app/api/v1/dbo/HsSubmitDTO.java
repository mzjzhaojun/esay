package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HsSubmitDTO {

	String memberid;
	String appid;
	String bankcode;
	String amount;
	String amount_true;
	String orderid;
	String sys_orderid;
	String datetime;
	String status;
	String sign_type;
	String sign;
	String attach;
}
