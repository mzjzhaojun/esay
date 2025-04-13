package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
* @author zj default
* 
* @version v1
* @createdate2025-04-13 22:38:13
*/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodetransferrecordDTO {


Long id;
Long tenant_id;
Long qrcodeid;
String qrcodename;
String outbizno;
String ordernum;
Object amount;
String payeeid;
String payeename;
String payeetype;
Integer status;
String fileid;
String dowloadurl;
Long create_by;
java.util.Date create_time;
Long update_by;
java.util.Date update_time;
String remark;
Integer version;
}