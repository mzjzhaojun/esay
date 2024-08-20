package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TgmerchantchannelmsgDTO {

	Long id;
	Long mid;
	Long cid;
	Integer chatid;
	String mmanger;
	String cmanger;
	Integer mreplyid;
	Integer creplyid;
	String ordernum;
	String orcode;
	Object amount;
	String telegrameimgid;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}