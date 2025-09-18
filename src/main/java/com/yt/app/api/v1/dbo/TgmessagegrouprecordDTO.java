package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-09-19 01:40:22
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgmessagegrouprecordDTO {

	Long id;
	Long mid;
	Long cid;
	Long chatid;
	String mmanger;
	String cmanger;
	Integer mreplyid;
	Integer creplyid;
	String name;
	String qrcode;
	Object exchange;
	Object amount;
	Object usd;
	Boolean status;
	String optionname;
	String confirmname;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}