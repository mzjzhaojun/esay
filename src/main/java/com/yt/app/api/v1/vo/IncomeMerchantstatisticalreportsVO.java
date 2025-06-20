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
 * @version v1 @createdate2024-09-02 12:01:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IncomeMerchantstatisticalreportsVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	Long merchantid;
	Double todayincome;
	Double incomecount;
	Double incomeuserpaycount;
	Double incomeuserpaysuccesscount;
	String name;
	Double payoutrate;
	Double balance;
	Integer todayorder;
	Integer successorder;
	Double todayorderamount;
	Double todaysuccessorderamount;
	String dateval;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}