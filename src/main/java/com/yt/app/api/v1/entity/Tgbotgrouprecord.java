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
 * @version v1 @createdate2024-04-03 16:45:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tgbotgrouprecord extends YtBaseEntity<Tgbotgrouprecord> {

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