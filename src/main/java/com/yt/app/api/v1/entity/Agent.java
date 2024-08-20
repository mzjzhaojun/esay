package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.yt.app.common.base.YtBaseEntity;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agent extends YtBaseEntity<Agent> {

	private static final long serialVersionUID = 1L;

	Long id;
	String name;
	String username;
	String password;
	String nkname;
	String remark;
	Double balance;
	Boolean status;
	Double exchange;
	Double onecost;
	Double downpoint;
	Integer downmerchantcount;
	String ipaddress;
	Long tenant_id;
	Long userid;
	Integer version;	
}