package com.yt.app.api.v1.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yt.app.common.base.BaseVO;

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
public class SysUserPermVO extends BaseVO {

	// ================= ↓↓↓↓↓↓ 基本信息 ↓↓↓↓↓↓ =================

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String twofactorcode;

	@JsonIgnore
	private String password;

	private String nickname;

	private Integer sex;
	
	private Integer twostatus;
	
	private String sexname;

	private String phone;

	private String email;

	private String avatar_url;

	private Date create_time;

	private Long dept_id;

	private List<Long> postIdList;

	private Long accounttype;

	// ================= ↓↓↓↓↓↓ 角色信息 ↓↓↓↓↓↓ =================

	@JsonIgnore
	private List<Long> roleIdList;

	private List<String> roleCodeList;

	// ================= ↓↓↓↓↓↓ 权限信息 ↓↓↓↓↓↓ =================

	private List<SysMenuTreeVO> permissionTreeList;

	// ================= ↓↓↓↓↓↓ 租户信息 ↓↓↓↓↓↓ =================

	private Long tenantId;

	private String tenantName;

	public void handleData() {
		/*
		 * this.sex = this.sexEnum.getType(); this.sexName = this.sexEnum.getDesc();
		 */
	}

}
