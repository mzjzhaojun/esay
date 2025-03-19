package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
* @author zj default
* 
* @version v1
* @createdate2025-03-19 19:51:13
*/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodestatisticalreportsDTO {


Long id;
Long tenant_id;
Long userid;
Long qrcodeid;
String name;
Object balance;
Object todayincome;
Object incomecount;
Integer todayorder;
Integer successorder;
Object todayorderamount;
Object todaysuccessorderamount;
Object incomeuserpaycount;
Object incomeuserpaysuccesscount;
Object payoutrate;
String dateval;
Long create_by;
java.util.Date create_time;
Long update_by;
java.util.Date update_time;
String remark;
Integer version;
}