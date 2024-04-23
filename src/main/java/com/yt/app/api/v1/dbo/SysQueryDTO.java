package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysQueryDTO {

	Integer merchantid;
	String merchantorderid;
	String remark;
	String sign;

	public SysQueryDTO() {
	}

	public SysQueryDTO(Integer merchantid, String merchantorderid, String remark, String sign) {
		super();
		this.merchantid = merchantid;
		this.merchantorderid = merchantorderid;
		this.remark = remark;
		this.sign = sign;
	}

}
