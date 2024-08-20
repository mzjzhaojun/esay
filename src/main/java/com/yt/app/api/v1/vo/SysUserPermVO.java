package com.yt.app.api.v1.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yt.app.api.v1.dbo.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统管理 - 用户信息
 * </p>
 *
 * @author zhengqingya
 * @description 基本信息+角色+权限...
 * @date 2020/4/15 10:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysUserPermVO extends BaseVO{

	// ================= ↓↓↓↓↓↓ 基本信息 ↓↓↓↓↓↓ =================

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户ID")
	private Long id;

	@ApiModelProperty(value = "账号")
	private String username;

	@ApiModelProperty(value = "二次")
	private String twofactorcode;

	@JsonIgnore
	@ApiModelProperty(value = "登录密码", hidden = true)
	private String password;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	private Integer sex;
	@ApiModelProperty(value = "性别")
	private String sexname;

	@ApiModelProperty(value = "手机号码")
	private String phone;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "头像")
	private String avatar_url;

	@ApiModelProperty(value = "创建时间")
	private Date create_time;

	@ApiModelProperty(value = "部门id")
	private Long dept_id;

	@ApiModelProperty(value = "岗位ids")
	private List<Long> postIdList;

	@ApiModelProperty(value = "账户类型")
	private Long accounttype;

	// ================= ↓↓↓↓↓↓ 角色信息 ↓↓↓↓↓↓ =================

	@JsonIgnore
	@ApiModelProperty(value = "角色ID", hidden = true)
	private List<Long> roleIdList;

	@ApiModelProperty("角色编码")
	private List<String> roleCodeList;

	// ================= ↓↓↓↓↓↓ 权限信息 ↓↓↓↓↓↓ =================

	@ApiModelProperty(value = "可访问的菜单+按钮权限")
	private List<SysMenuTreeVO> permissionTreeList;

	// ================= ↓↓↓↓↓↓ 租户信息 ↓↓↓↓↓↓ =================

	@ApiModelProperty(value = "租户ID")
	private Long tenantId;

	@ApiModelProperty(value = "租户名")
	private String tenantName;

	public void handleData() {
		/*
		 * this.sex = this.sexEnum.getType(); this.sexName = this.sexEnum.getDesc();
		 */
	}

}
