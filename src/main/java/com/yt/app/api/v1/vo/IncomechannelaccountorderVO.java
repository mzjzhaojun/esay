package com.yt.app.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.BaseVO;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 00:41:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IncomechannelaccountorderVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long channelid;
	String channelname;
	String nkname;
	String channelcode;
	String ordernum;
	Integer type;
	Double deal;
	Double onecost;
	Double amount;
	String accname;
	String accnumber;
	Double exchange;
	Double channelexchange;
	Double amountreceived;
	Double usdtval;
	Integer status;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
	String imgurl;
}