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
public class SysSnOrder extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer Code;
	String Message;
	data Data;

	@Data
	public class data {
		String MerchantOrderNo;
		String OrderNo;
		Integer OrderStatus;
		Double Amount;
		Integer Status;
		String Attachments;
	}

}
