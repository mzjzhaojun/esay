package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 18:43:33
 */
@Getter
@Setter
public class Merchantcustomerbanks extends YtBaseEntity<Merchantcustomerbanks> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String accname;
	String accnumber;
	String bankcode;
	String bankname;
	String bankaddress;
	String value;
	Integer version;

	public Merchantcustomerbanks() {
	}

	public Merchantcustomerbanks(Long id, Long tenant_id, Long userid, String accname, String accnumber,
			String bankname, String bankaddress, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, Integer version) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.userid = userid;
		this.accname = accname;
		this.accnumber = accnumber;
		this.bankname = bankname;
		this.bankaddress = bankaddress;
		this.version = version;
	}

}