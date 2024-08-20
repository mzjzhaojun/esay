package com.yt.app.api.v1.vo;

import com.yt.app.common.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-02 18:47:41
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AislechannelVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long aisleid;
	Long channelid;
	Long tenant_id;
	String name;
	String nkname;
	String code;
	Boolean status;
	Double balance;
	Double exchange;
	Double onecost;
	Double downpoint;
	Double costexchange;
	Integer min;
	Integer max;
	Integer weight;
	Integer mtorder;
	Boolean balancesynch;
	Boolean firstmatch;
	String firstmatchmoney;
	String apiip;
	String apikey;
	String apireusultip;
	String remark;
	Integer version;

}