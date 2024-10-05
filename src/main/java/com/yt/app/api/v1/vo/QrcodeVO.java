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
public class QrcodeVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String name;
	String nkname;
	String mobile;
	String code;
	Boolean status;
	Double balance;
	Integer expireminute;
	String type;
	String typename;
	Integer min;
	Integer max;
	Integer weight;
	Integer mtorder;
	Boolean dynamic;
	Boolean accountsplit;
	String fixedcode;
	Boolean firstmatch;
	String firstmatchmoney;
	Integer yestodayorder;
	Double yestodayincome;
	Integer todayorder;
	Double todayincome;
	String appid;
	String notifyurl;
	String appprivatekey;
	String apppublickey;
	String alipaypublickey;
	String alipayprovatekey;
	Double limits;
	Double todaylimits;
	Double incomesum;
	Integer ordersum;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	String payurl;
	Integer version;
}