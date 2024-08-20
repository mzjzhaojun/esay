package com.yt.app.api.v1.vo;

import com.yt.app.api.v1.dbo.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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

	@ApiModelProperty(value = "主键")
	private Integer id;

	@ApiModelProperty(value = "字典类型编码")
	private String code;

	@ApiModelProperty(value = "字典类型名称(展示用)")
	private String name;

	@ApiModelProperty(value = "排序")
	private Integer sort;

}
