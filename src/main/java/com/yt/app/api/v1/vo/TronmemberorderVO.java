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
 * @version v1 @createdate2024-10-15 00:23:49
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TronmemberorderVO extends BaseVO {

	private static final long serialVersionUID = 1L;

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