package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-10-15 00:23:49
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TronmemberorderDTO {

	Long id;
	Long tenant_id;
	Long chatid;
	Long messageid;
	Long tgid;
	String ordernum;
	Integer type;
	String goodsname;
	String fromaddress;
	Object amount;
	Object realamount;
	Object trxamount;
	Object usdtamount;
	Object fewamount;
	String incomeaddress;
	Integer status;
	String remark;
	java.util.Date successdate;
	java.util.Date expireddate;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}