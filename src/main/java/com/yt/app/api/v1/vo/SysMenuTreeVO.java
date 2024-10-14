package com.yt.app.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.yt.app.common.base.BaseVO;

/**
 * <p>
 * 菜单权限树
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 20:54
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysMenuTreeVO extends BaseVO {

	// ==================== ↓↓↓↓↓↓ 菜单基础信息 ↓↓↓↓↓↓ ====================

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long parent_id;

	private String name;

	private String fullName;

	private String icon;

	private String path;

	private String btn_perm;

	private Integer sort;

	private String component;

	private String redirect;

	private Integer is_show;

	private Integer is_show_breadcrumb;

	private Integer version;

	private List<SysMenuTreeVO> children;

	private Meta meta;

	/**
	 * {@link com.zhengqing.system.enums.SysMenuTypeEnum}
	 */
	private Integer type;

	// ==================== ↓↓↓↓↓↓ 权限信息 ↓↓↓↓↓↓ ====================

	private Boolean isHasPerm;

	@Getter
	@Setter
	@SuperBuilder
	public static class Meta {
		private Integer isShow;

		private Integer sort;

		private String title;

		private String icon;

		private Integer isShowBreadcrumb;
	}

	public void handleData() {
		this.children = CollectionUtils.isEmpty(this.children) ? Lists.newArrayList() : this.children;

		this.meta = Meta.builder().isShow(this.is_show).sort(this.sort).title(this.name).icon(this.icon).isShowBreadcrumb(this.is_show_breadcrumb).build();
	}

}
