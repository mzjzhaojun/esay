package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */
@Getter
@Setter
public class Tgmerchantgroup extends YtBaseEntity<Tgmerchantgroup> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String merchantname;
	String merchantcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String remark;
	Integer version;

	public Tgmerchantgroup() {
	}

	public Tgmerchantgroup(Long id, Long tenant_id, String merchantname, String merchantcode, Boolean status,
			Long tgid, String tggroupname, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.merchantname = merchantname;
		this.merchantcode = merchantcode;
		this.status = status;
		this.tgid = tgid;
		this.tggroupname = tggroupname;
		this.remark = remark;
		this.version = version;
	}
}