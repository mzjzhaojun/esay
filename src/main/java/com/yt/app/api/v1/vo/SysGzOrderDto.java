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
public class SysGzOrderDto extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String out_trade_no;
	String amount;
	String channel;
	String notify_url;
	String return_url;
	String name;
}
