package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 00:41:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IncomechannelaccountrecordDTO {

	Long id;
	Long tenant_id;
	String channelname;
	Long userid;
	String ordernum;
	Integer type;
	Object pretotalincome;
	Object prewithdrawamount;
	Object pretowithdrawamount;
	Object pretoincomeamount;
	Object posttotalincome;
	Object postwithdrawamount;
	Object posttowithdrawamount;
	Object posttoincomeamount;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}