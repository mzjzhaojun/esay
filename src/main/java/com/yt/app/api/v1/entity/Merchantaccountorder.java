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
 * @version v1 @createdate2023-11-15 09:44:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

	
}