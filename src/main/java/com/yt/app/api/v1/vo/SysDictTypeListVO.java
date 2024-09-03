package com.yt.app.api.v1.vo;

import com.yt.app.common.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 数据字典类型-响应参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/15 4:37 下午
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeListVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String code;

	private String name;

	private Integer sort;

}
