package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 18:22:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Income extends YtBaseEntity<Income> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String ordernum;
	Long merchantid;
	String merchantname;
	Long channelid;
	String merchantorderid;
	String backforwardurl;
	java.util.Date expireddate;
	Integer expiredminute;
	Long qrcodeaisleid;
	String qrcodeaislecode;
	String qrcodeaislename;
	Long agentid;
	String channelname;
	Double agentincome;
	Long qrcodeid;
	String qrcodecode;
	String qrcodename;
	String qrcodeordernum;
	String mobile;
	String accnumer;
	String accname;
	Double amount;
	Double collection;
	Double exchange;
	Double realamount;
	Integer status;
	java.util.Date successtime;
	Long backlong;
	String qrcode;
	String type;
	Boolean dynamic;
	String resulturl;
	Double fewamount;
	String notifyurl;
	Integer notifystatus;
	String inipaddress;
	String blockaddress;
	String remark;
	Double incomeamount;
	Double merchantincomeamount;
	Double channelincomeamount;
	Integer version;
}