package com.yt.app.api.v1.vo;



import com.yt.app.common.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TgmerchantchannelmsgVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long mid;
	Long cid;
	Integer chatid;
	String mmanger;
	String cmanger;
	Integer mreplyid;
	Integer creplyid;
	String ordernum;
	String qrcode;
	Double amount;
	String telegrameimgid;
	Double exchange;
	Double usd;
	String remark;
	Integer version;
}