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
 * @version v1 @createdate2024-03-31 17:29:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tgbot extends YtBaseEntity<Tgbot> {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String name;
	String token;
	String manger;
	Boolean status;
	Integer type;
	String expstr;
	java.util.Date exp_time;
	String remark;
	String welcomemsg;
	Integer version;

}