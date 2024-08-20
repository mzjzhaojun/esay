package com.yt.app.api.v1.vo;



import com.yt.app.api.v1.dbo.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TgbotgrouprecordVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long tgid;
	String tgname;
	String gmanger;
	String xmanger;
	Boolean tmexchange;
	double exchange;
	double cost;
	Boolean status;
	double amount;
	double withdrawusdt;
	Integer type;
	String remark;
	Integer version;

}