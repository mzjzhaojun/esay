package com.yt.app.api.v1.dbo;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeSubmitDTO {

	String pay_memberid;
	String pay_orderid;
	String pay_aislecode;
	String pay_amount;
	String pay_notifyurl;
	String pay_callbackurl;
	String pay_productname;
	String pay_productnum;
	String pay_productdesc;
	String pay_producturl;
	String pay_attach;
	String pay_md5sign;
	String pay_applydate;
	JSONObject cardInfo;
}
