package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:06
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CrownagentDTO {

	Long id;
	Long tenant_id;
	String name;
	String nikname;
	String code;
	Boolean status;
	String type;
	Integer level;
	Long channelid;
	String cookie;
	String username;
	String password;
	String email;
	String domian;
	String uid;
	String ver;
	String selmaxid;
	String origin;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}