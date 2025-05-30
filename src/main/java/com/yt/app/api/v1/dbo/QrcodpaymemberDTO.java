package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-16 23:44:12
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodpaymemberDTO {

	Long id;
	Long tenant_id;
	Long qrcodeid;
	String qrcodename;
	String mobile;
	String name;
	String pcardno;
	String cardno;
	String smsno;
	String protocolno;
	Integer status;
	String memberid;
	String token;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}