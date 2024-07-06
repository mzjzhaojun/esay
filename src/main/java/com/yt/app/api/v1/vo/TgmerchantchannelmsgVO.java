package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
 */
@Getter
@Setter
public class TgmerchantchannelmsgVO extends YtBaseEntity<TgmerchantchannelmsgVO> {

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

	public TgmerchantchannelmsgVO() {
	}

	public TgmerchantchannelmsgVO(Long id, Long mid, Long cid, Integer chatid, String mmanger, String cmanger,
			Integer mreplyid, Integer creplyid, String ordernum, String qrcode, Double amount, String telegrameimgid,
			String remark, Long create_by, java.util.Date create_time, Long update_by, java.util.Date update_time,
			Integer version) {
		this.id = id;
		this.mid = mid;
		this.cid = cid;
		this.chatid = chatid;
		this.mmanger = mmanger;
		this.cmanger = cmanger;
		this.mreplyid = mreplyid;
		this.creplyid = creplyid;
		this.ordernum = ordernum;
		this.qrcode = qrcode;
		this.amount = amount;
		this.telegrameimgid = telegrameimgid;
		this.remark = remark;
		this.version = version;
	}
}