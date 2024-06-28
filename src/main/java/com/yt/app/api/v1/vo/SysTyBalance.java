package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysTyBalance {

	Integer merchantID;
	Double fronzenBalance;
	Double balance;
	Double availableBalance;

	String sign;

	public SysTyBalance() {
	}

}
