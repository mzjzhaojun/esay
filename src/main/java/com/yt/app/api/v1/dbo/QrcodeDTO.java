package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeDTO {

	Long id;
	Long tenant_id;
	Long userid;
	String name;
	String nkname;
	String mobile;
	String code;
	Boolean status;
	Object balance;
	Object onecost;
	Integer type;
	Integer min;
	Integer max;
	Integer weight;
	Integer mtorder;
	Boolean dynamic;
	String smid;
	Boolean firstmatch;
	String firstmatchmoney;
	Long create_by;
	java.util.Date create_time;
	Long update_by;
	java.util.Date update_time;
	String remark;
	Integer version;
}