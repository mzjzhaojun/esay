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
	Long merchantuserid;
	String ordernum;
	Long channelid;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	String merchantorderid;
	String backforwardurl;
	java.util.Date expireddate;
	Integer expiredminute;
	Long qrcodeaisleid;
	String qrcodeaislecode;
	String qrcodeaislename;
	Long agentid;
	String agentordernum;
	Double agentincome;
	Long qrcodeid;
	String qrcodename;
	String qrcodeordernum;
	Long qrcodeuserid;
	Double amount;
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
	String remark;
	Double incomeamount;
	Double merchantincomeamount;
	Double channelincomeamount;
	Integer version;
}