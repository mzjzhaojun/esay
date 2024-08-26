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
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IncomemerchantaccountrecordVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String merchantname;
	String ordernum;
	Integer type;
	String typename;
	Object pretotalincome;
	Object pretoincomeamount;
	Object prewithdrawamount;
	Object pretowithdrawamount;
	Object posttotalincome;
	Object posttoincomeamount;
	Object postwithdrawamount;
	Object posttowithdrawamount;
	java.util.Date update_time;
	Long update_by;
	java.util.Date create_time;
	Long create_by;
	String remark;
	Integer version;
}