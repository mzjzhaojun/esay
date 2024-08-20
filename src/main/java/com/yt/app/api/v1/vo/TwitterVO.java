package com.yt.app.api.v1.vo;

import com.yt.app.api.v1.dbo.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-02 18:47:41
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TwitterVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	Integer type;
	String label;
	String name;
	String code;
	String remark;
	Long create_by;
	java.util.Date create_time;
	String img;
	Integer version;

}