package com.yt.app.api.v1.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 数据权限
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/11/28 23:16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysScopeDataBO {
	private String scopeColumn;

	private String scopeVisibleField;

	private String scopeClass;

	/**
	 * {@link com.zhengqing.common.db.enums.DataPermissionTypeEnum}
	 */
	private Integer scopeType;

	private String scopeValue;
}