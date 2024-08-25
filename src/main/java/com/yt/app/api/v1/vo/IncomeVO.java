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
 * @version v1 @createdate2024-08-23 18:22:46
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IncomeVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long merchantuserid;
	String ordernum;
	Long merchantid;
	String merchantname;
	String merchantcode;
	String merchantordernum;
	String backforwardurl;
	java.util.Date expireddate;
	Integer expiredminute;
	Long qrcodeaisleid;
	String qrcodeaislename;
	Long agentid;
	String agentordernum;
	Object agentincome;
	Object qrcodeid;
	String qrcodename;
	String qrcodeordernum;
	String qrcodeuserid;
	Object amount;
	Object realamount;
	Integer status;
	String statusname;
	java.util.Date successtime;
	String merchantorderid;
	Long backlong;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String qrcode;
	String type;
	String typename;
	String resulturl;
	Object fewamount;
	String notifyurl;
	Integer notifystatus;
	String notifystatusname;
	String remark;
	Double incomeamount;
	Double merchantincomeamount;
	Double channelincomeamount;
	Integer version;
}