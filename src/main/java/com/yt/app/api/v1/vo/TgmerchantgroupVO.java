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
 * @version v1 @createdate2024-07-05 13:07:39
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TgmerchantgroupVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long channelid;
	String channelname;
	Long merchantid;
	String merchantname;
	String merchantcode;
	Boolean status;
	Long tgid;
	String tggroupname;
	String adminmangers;
	String mangers;
	String customermangers;
	Double cost;
	Double realtimeexchange;
	Double costcount;
	Double usdcount;
	Integer todaycountorder;
	Double todayusdcount;
	Integer countorder;
	Double todaycount;
	Double count;
	String remark;
	String version;

}