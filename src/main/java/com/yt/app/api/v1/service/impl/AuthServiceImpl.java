package com.yt.app.api.v1.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

import com.yt.app.api.v1.bo.JwtUserBO;
import com.yt.app.api.v1.bo.SysScopeDataBO;
import com.yt.app.api.v1.dbo.AuthLoginDTO;
import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.Logs;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.SystemaccountMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.AuthService;
import com.yt.app.api.v1.service.LogsService;
import com.yt.app.api.v1.service.RoleService;
import com.yt.app.api.v1.service.RolescopeService;
import com.yt.app.api.v1.service.UserService;
import com.yt.app.api.v1.vo.AuthLoginVO;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.enums.AuthSourceEnum;
import com.yt.app.common.enums.SysRoleCodeEnum;
import com.yt.app.common.enums.YtCodeEnum;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.AuthUtil;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.RsaUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 授权 服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 11:33
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private RoleService sysroleservice;

	@Autowired
	private UserService isysuserservice;

	@Autowired
	private LogsService logsservice;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private RolescopeService tsysrolescopeservice;

	@Autowired
	private SystemaccountMapper systemaccountmapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public AuthLoginVO login(AuthLoginDTO params) {

		String username = params.getUsername();
		String password = params.getPassword();
		SysUserPermVO userPerm = sysroleservice.getUserPerm(SysUserPermDTO.builder().username(username).build());
		boolean isValid = PasswordUtil.isValidPassword(password, userPerm.getPassword());

		// 校验原始密码是否正确
		Assert.isTrue(isValid, "密码错误！");

		if (userPerm.getTwostatus() == 1) {
			isValid = GoogleAuthenticatorUtil.checkCode(userPerm.getTwofactorcode(), Long.parseLong(params.getCode()), System.currentTimeMillis());
			Assert.isTrue(isValid, "验证码错误！");
		}

		// 拿到下级角色ids
		List<Long> roleIdList = userPerm.getRoleIdList();
		Assert.isTrue(CollUtil.isNotEmpty(roleIdList), "无权限，请先分配权限！");
		List<String> roleCodeList = userPerm.getRoleCodeList();
		List<Long> allRoleIdList = new ArrayList<Long>();
		allRoleIdList.addAll(roleIdList);
		if (!roleCodeList.contains(SysRoleCodeEnum.超级管理员.getCode())) {
			for (int i = 0; i < roleIdList.size(); i++) {
				roleIdList.forEach(roleIdItem -> allRoleIdList.addAll(this.sysroleservice.getChildRoleIdList(roleIdItem)));
			}

		}
		// 去重
		List<Long> allRoleIdListFinal = allRoleIdList.stream().distinct().collect(Collectors.toList());

		// 根据角色拿到关联的数据权限
		List<SysScopeDataBO> scopeDataList = this.tsysrolescopeservice.getScopeListReRoleIdList(roleIdList);

		// 查询系统账户id
		Systemaccount sca = systemaccountmapper.getByTenantId(userPerm.getTenantId());

		// 写入登录日志
		logsservice.post(Logs.builder().optname(username).optdate(new Date()).requestip(AuthContext.getIp()).type(DictionaryResource.LOG_TYPE_201).build());
		// 登录
		return AuthUtil.login(JwtUserBO.builder().authSourceEnum(AuthSourceEnum.B).userId(Long.valueOf(userPerm.getId())).username(userPerm.getUsername()).allRoleIdList(allRoleIdListFinal).roleCodeList(roleCodeList).deptId(userPerm.getDept_id())
				.scopeDataList(scopeDataList).tenantId(userPerm.getTenantId()).systemaccountId(sca.getId()).accounttype(userPerm.getAccounttype()).build());
	}

	public String genQRImage(String username) {
		return GoogleAuthenticatorUtil.getQrCodeText(GoogleAuthenticatorUtil.getSecretKey(), username, "");
	}

	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Integer verqrcode(String username, String password, String code, String twocode) {
		Integer i = 0;
		User u = usermapper.getByUserName(username);
		Assert.notNull(u, "用户没找到！");

		boolean isValid = PasswordUtil.isValidPassword(password, u.getPassword());
		// 校验原始密码是否正确
		Assert.isTrue(isValid, "密码错误！");
		u.setTwostatus(1);
		u.setTwofactorcode(twocode);
		boolean flag = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(code), System.currentTimeMillis());
		if (flag) {
			usermapper.put(u);
			return 1;
		}
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public AuthLoginVO loginapp(AuthLoginDTO params) {
		String username = params.getUsername();
		String password = params.getPassword();
		SysUserPermVO userPerm = this.isysuserservice.getUserPerm(SysUserPermDTO.builder().username(username).build());
		boolean isValid = PasswordUtil.isValidPassword(password, userPerm.getPassword());
		// 校验原始密码是否正确
		Assert.isTrue(isValid, "密码错误！");

		if (userPerm.getTwostatus() == 1) {
			isValid = GoogleAuthenticatorUtil.checkCode(userPerm.getTwofactorcode(), Long.parseLong(params.getCode()), System.currentTimeMillis());
			Assert.isTrue(isValid, "验证码错误！");
		}

		// 写入登录日志
		logsservice.post(Logs.builder().optname(username).optdate(new Date()).requestip(AuthContext.getIp()).type(DictionaryResource.LOG_TYPE_202).build());

		return AuthUtil.login(JwtUserBO.builder().authSourceEnum(AuthSourceEnum.B).userId(Long.valueOf(userPerm.getId())).username(userPerm.getUsername()).deptId(userPerm.getDept_id()).tenantId(userPerm.getTenantId())
				.accounttype(userPerm.getAccounttype()).build());
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public String getPublicKey(HttpServletRequest request) {
		String ip = AuthContext.getIp();
		boolean isValid = false;
		// 判断ip 是否在配置内
		List<Merchant> listm = merchantmapper.getListAll(new HashMap<String, Object>());
		for (int i = 0; i < listm.size(); i++) {
			if (listm.get(i).getIpaddress() != null && listm.get(i).getIpaddress().indexOf(ip) >= 0) {
				isValid = true;
				break;
			}
		}
		if (!isValid)
			throw new YtException(YtCodeEnum.YT402.getDesc(), YtCodeEnum.YT402);
		return RsaUtil.getPublicKey();
	}

}
