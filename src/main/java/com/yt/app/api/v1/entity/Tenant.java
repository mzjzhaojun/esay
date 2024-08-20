package com.yt.app.api.v1.entity;





import com.yt.app.common.base.YtBaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-23 20:33:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tenant extends YtBaseEntity<Tenant> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	Long admin_user_id;
	String admin_name;
	String admin_phone;
	Boolean status;
	Long package_id;
	java.util.Date expire_time;
	Integer account_count;
	Integer job_num;
	Integer sort;
	Integer is_deleted;
	Integer version;

	String username;

	String password;

}