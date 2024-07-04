package com.yt.app.api.v1.dbo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 17:05:13
 */
@Getter
@Setter
public class TgmerchantgroupDTO {

	Long id;
	Long tenant_id;
	Long merchantid;
	String merchantname;
	String merchantcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String osmangers;
	String customermangers;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;

	public TgmerchantgroupDTO() {
	}

	public TgmerchantgroupDTO(Long id, Long tenant_id, Long merchantid, String merchantname, String merchantcode,
			Boolean status, Long tgid, String tggroupname, String osmangers, String customermangers, Long create_by,
			java.util.Date create_time, Long update_by, java.util.Date update_time, String remark, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.merchantid = merchantid;
		this.merchantname = merchantname;
		this.merchantcode = merchantcode;
		this.status = status;
		this.tgid = tgid;
		this.tggroupname = tggroupname;
		this.osmangers = osmangers;
		this.customermangers = customermangers;
		this.create_by = create_by;
		this.create_time = create_time;
		this.update_by = update_by;
		this.update_time = update_time;
		this.remark = remark;
		this.version = version;
	}
}