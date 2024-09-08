package com.yt.app.api.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import com.yt.app.common.base.YtBaseEntity;
/**
* @author zj default
* 
* @version v1
* @createdate2024-09-08 01:31:33
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tronrecord extends YtBaseEntity<Tronrecord>{

private static final long serialVersionUID=1L;

Long id;
Long tenant_id;
Integer type;
String owneraddress;
String toaddress;
Boolean status;
Object usdtamount;
Object sunamount;
Object trxamount;
String remark;
Long create_by;
java.util.Date create_time;
Long update_by;
java.util.Date update_time;
Integer version;
}