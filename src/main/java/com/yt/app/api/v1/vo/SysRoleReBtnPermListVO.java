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
 * 角色 -- url/btn权限
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/10 22:03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleReBtnPermListVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Integer menuId;

	private String path;

	private String btnPerm;

	private List<String> roleCodeList;

}
