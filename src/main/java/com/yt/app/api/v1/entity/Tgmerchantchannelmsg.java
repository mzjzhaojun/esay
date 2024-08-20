package com.yt.app.api.v1.entity;




import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tgmerchantchannelmsg extends YtBaseEntity<Tgmerchantchannelmsg> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long mid;
	Long cid;
	Long chatid;
	String mmanger;
	String cmanger;
	Integer mreplyid;
	Integer creplyid;
	String ordernum;
	String qrcode;
	Double exchange;
	Double usd;
	Double amount;
	String telegrameimgid;
	String remark;
	Integer version;

}