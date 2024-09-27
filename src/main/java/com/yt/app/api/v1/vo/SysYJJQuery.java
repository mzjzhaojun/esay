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
public class SysYJJQuery extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String code;
	String msg;
	data data;

	@Data
	public class data {
		String merchant_id;
		String order_id;
		String order_no;
		String amount;
		String status;
		String pay_at;
	}
}
