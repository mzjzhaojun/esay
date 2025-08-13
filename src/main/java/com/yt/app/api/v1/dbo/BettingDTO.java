package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BettingDTO {

	Long id;
	Long tenant_id;
	String wagerstype;
	String tid;
	String teamc;
	String teamh;
	String site;
	String showtextordertypeioratio;
	String showtextleague;
	String ordertype;
	String numc;
	String numh;
	String name;
	String gt;
	String gold;
	String aresult;
	String inradio;
	String remark;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	Integer version;
}