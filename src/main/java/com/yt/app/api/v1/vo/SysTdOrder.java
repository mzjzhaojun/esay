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
public class SysTdOrder extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String status;
	String msg;
	data data;

	@Data
	public class data {
		String pay_url;
		String pay_type;
	}
}
