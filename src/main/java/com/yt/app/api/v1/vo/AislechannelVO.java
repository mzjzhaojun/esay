package com.yt.app.api.v1.vo;

import lombok.Getter;
import lombok.Setter;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-02 18:47:41
 */
@Getter
@Setter
public class AislechannelVO extends YtBaseEntity<AislechannelVO> {

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

	public AislechannelVO() {
	}

	public AislechannelVO(Long id, Long aisleid, Long channelid, Long tenant_id, String name, String nkname,
			String code, Boolean status, Double balance, Double exchange, Double onecost, Double downpoint,
			Double costexchange, Integer min, Integer max, Integer weight, Integer mtorder, Boolean balancesynch,
			Boolean firstmatch, String firstmatchmoney, String apiip, String apikey, String apireusultip, String remark,
			Integer version) {
		super();
		this.id = id;
		this.aisleid = aisleid;
		this.channelid = channelid;
		this.tenant_id = tenant_id;
		this.name = name;
		this.nkname = nkname;
		this.code = code;
		this.status = status;
		this.balance = balance;
		this.exchange = exchange;
		this.onecost = onecost;
		this.downpoint = downpoint;
		this.costexchange = costexchange;
		this.min = min;
		this.max = max;
		this.weight = weight;
		this.mtorder = mtorder;
		this.balancesynch = balancesynch;
		this.firstmatch = firstmatch;
		this.firstmatchmoney = firstmatchmoney;
		this.apiip = apiip;
		this.apikey = apikey;
		this.apireusultip = apireusultip;
		this.remark = remark;
		this.version = version;
	}

}