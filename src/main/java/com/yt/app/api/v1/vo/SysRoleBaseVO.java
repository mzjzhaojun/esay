package com.yt.app.api.v1.vo;

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

	private Long parent_id;

	private Long id;

	private String name;

	private String code;

	private Integer status;

	private Boolean is_fixed;

	private Integer sort;

	private Boolean is_refresh_rll_tenant;

	private List<SysRoleBaseVO> children;

	public void handleData() {

	}

}
