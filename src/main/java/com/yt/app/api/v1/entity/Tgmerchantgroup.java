package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 17:05:13
 */
@Getter
@Setter
public class Tgmerchantgroup extends YtBaseEntity<Tgmerchantgroup> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long channelid;
	String channelname;
	Long merchantid;
	String merchantname;
	String merchantcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String adminmangers;
	String mangers;
	String customermangers;
	String remark;
	Integer version;

	public Tgmerchantgroup() {
	}

	public Tgmerchantgroup(Long id, Long tenant_id, Long merchantid, String merchantname, String merchantcode,
			Boolean status, Long tgid, String tggroupname, String mangers, String customermangers, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.merchantid = merchantid;
		this.merchantname = merchantname;
		this.merchantcode = merchantcode;
		this.status = status;
		this.tgid = tgid;
		this.tggroupname = tggroupname;
		this.mangers = mangers;
		this.customermangers = customermangers;
		this.remark = remark;
		this.version = version;
	}
}