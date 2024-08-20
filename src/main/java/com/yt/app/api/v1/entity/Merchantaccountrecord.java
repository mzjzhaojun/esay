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
 * @version v1 @createdate2023-11-15 09:44:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchantaccountrecord extends YtBaseEntity<Merchantaccountrecord> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Long userid;
	String merchantname;
	String ordernum;
	Integer type;
	String typename;
	Double pretotalincome;
	Double prewithdrawamount;
	Double pretowithdrawamount;
	Double pretoincomeamount;
	Double posttotalincome;
	Double postwithdrawamount;
	Double posttowithdrawamount;
	Double posttoincomeamount;
	String remark;
	Integer version;

	
}