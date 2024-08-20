package com.yt.app.api.v1.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.yt.app.common.base.BaseVO;

/**
 * <p>
 * 系统管理-角色表-输出内容
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 16:19
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleBaseVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("父id")
	private Long parent_id;

	@ApiModelProperty(value = "角色ID")
	private Long id;

	@ApiModelProperty(value = "角色名")
	private String name;

	@ApiModelProperty(value = "角色编码")
	private String code;

	@ApiModelProperty(value = "状态(1:开启 0:禁用)")
	private Integer status;

	@ApiModelProperty(value = "是否固定(false->否 true->是)")
	private Boolean is_fixed;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "是否刷新所有租户权限数据(false->否 true->是)")
	private Boolean is_refresh_rll_tenant;

	@ApiModelProperty("子级")
	private List<SysRoleBaseVO> children;

	public void handleData() {

	}

}
