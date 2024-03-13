package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */
@Getter
@Setter
public class Merchantaccountorder extends YtBaseEntity<Merchantaccountorder> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long merchantid;
	String username;
	String nkname;
	String merchantcode;
	Double deal;
	Double amount;
	Double onecost;
	String accname;
	String accnumber;
	Integer type;
	Double exchange;
	Double merchantexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String statusname;
	String remark;
	String imgurl;
	Integer version;
	String ordernum;

	public Merchantaccountorder() {
	}

	public Merchantaccountorder(Long id, Long tenant_id, Long merchantid, String username, String nkname,
			String merchantcode, Double amount, Double exchange, Double merchantexchange, Double amountreceived,
			Integer status, String remark, Long create_by, java.util.Date create_time, Long update_by,
			java.util.Date update_time, Integer version, String ordernum) {
		this.id = id;
		this.tenant_id = tenant_id;
		this.merchantid = merchantid;
		this.username = username;
		this.nkname = nkname;
		this.merchantcode = merchantcode;
		this.amount = amount;
		this.exchange = exchange;
		this.merchantexchange = merchantexchange;
		this.amountreceived = amountreceived;
		this.status = status;
		this.remark = remark;

		this.version = version;
		this.ordernum = ordernum;
	}
}