package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
 */
@Getter
@Setter
public class TgmerchantchannelmsgDTO {

	Long id;
	Long mid;
	Long cid;
	Integer chatid;
	String mmanger;
	String cmanger;
	Integer mreplyid;
	Integer creplyid;
	String ordernum;
	String orcode;
	Object amount;
	String telegrameimgid;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;

	public TgmerchantchannelmsgDTO() {
	}

	public TgmerchantchannelmsgDTO(Long id, Long mid, Long cid, Integer chatid, String mmanger, String cmanger,
			Integer mreplyid, Integer creplyid, String ordernum, String orcode, Object amount, String telegrameimgid,
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
		this.orcode = orcode;
		this.amount = amount;
		this.telegrameimgid = telegrameimgid;
		this.remark = remark;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.version = version;
	}
}