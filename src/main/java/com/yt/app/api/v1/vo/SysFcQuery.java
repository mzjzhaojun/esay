package com.yt.app.api.v1.vo;

import com.yt.app.common.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysFcQuery extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String code;
	String message;
	String sign;
	data data;

	@Data
	public class data {
		String mchId;
		String tradeNo;
		String outTradeNo;
		String amount;
		String payUrl;
		String state;
		String balance;
	}
}
