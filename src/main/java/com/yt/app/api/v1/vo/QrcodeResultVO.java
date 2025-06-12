package com.yt.app.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.BaseVO;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QrcodeResultVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	String pay_memberid;
	String pay_orderid;
	String pay_aislecode;
	String pay_amount;
	String pay_viewurl;
	String pay_md5sign;
//	String pay_outordernum;
}