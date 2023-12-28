package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-28 15:01:59
 */
@Getter
@Setter
public class Tgmerchantchannelmsg extends YtBaseEntity<Tgmerchantchannelmsg> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long mid;
	Long cid;
	String mmanger;
	String cmanger;
	String ordernum;
	Integer mreplyid;
	Integer creplyid;
	Integer version;

	public Tgmerchantchannelmsg() {
	}

	public Tgmerchantchannelmsg(Long id, Long mid, Long cid, String mmanger, String cmanger, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, String ordernum, Integer version) {
		this.id = id;
		this.mid = mid;
		this.cid = cid;
		this.mmanger = mmanger;
		this.cmanger = cmanger;
		this.ordernum = ordernum;
		this.version = version;
	}
}