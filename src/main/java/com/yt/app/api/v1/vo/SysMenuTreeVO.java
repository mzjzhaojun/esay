package com.yt.app.api.v1.vo;

import io.swagger.annotations.ApiModelProperty;
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
import com.yt.app.api.v1.dbo.base.BaseVO;

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
public class SysMenuTreeVO extends BaseVO{

	// ==================== ↓↓↓↓↓↓ 菜单基础信息 ↓↓↓↓↓↓ ====================

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("ID")
	private Long id;

	@ApiModelProperty("父ID")
	private Long parent_id;

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("全路径名称( eg: /系统管理/用户管理 )")
	private String fullName;

	@ApiModelProperty("图标")
	private String icon;

	@ApiModelProperty("访问路径")
	private String path;

	@ApiModelProperty(value = "按钮权限标识")
	private String btn_perm;

	@ApiModelProperty("显示顺序")
	private Integer sort;

	@ApiModelProperty("组件")
	private String component;

	@ApiModelProperty("重定向url")
	private String redirect;

	@ApiModelProperty("是否显示(1:显示 0:隐藏)")
	private Integer is_show;

	@ApiModelProperty("是否显示面包屑(1:显示 0:隐藏)")
	private Integer is_show_breadcrumb;

	@ApiModelProperty("是否显示(1:显示 0:隐藏)")
	private Integer version;

	@ApiModelProperty("下级菜单")
	private List<SysMenuTreeVO> children;

	@ApiModelProperty("路由元信息(前端用于菜单显示)")
	private Meta meta;

	/**
	 * {@link com.zhengqing.system.enums.SysMenuTypeEnum}
	 */
	@ApiModelProperty(value = "类型")
	private Integer type;

	// ==================== ↓↓↓↓↓↓ 权限信息 ↓↓↓↓↓↓ ====================

	@ApiModelProperty("是否具有菜单权限")
	private Boolean isHasPerm;

	@Getter
	@Setter
	@SuperBuilder
	public static class Meta {
		@ApiModelProperty("是否显示(1:显示 0:隐藏)")
		private Integer isShow;

		@ApiModelProperty("显示顺序")
		private Integer sort;

		@ApiModelProperty("标题")
		private String title;

		@ApiModelProperty("图标")
		private String icon;

		@ApiModelProperty("是否显示面包屑(1:显示 0:隐藏)")
		private Integer isShowBreadcrumb;
	}

	public void handleData() {
		this.children = CollectionUtils.isEmpty(this.children) ? Lists.newArrayList() : this.children;

		this.meta = Meta.builder().isShow(this.is_show).sort(this.sort).title(this.name).icon(this.icon)
				.isShowBreadcrumb(this.is_show_breadcrumb).build();
	}

}
