package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountbankMapper;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.AgentaccountorderService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountbank;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

@Service
public class AgentaccountorderServiceImpl extends YtBaseServiceImpl<Agentaccountorder, Long> implements AgentaccountorderService {

	@Autowired
	private AgentaccountorderMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private AgentMapper agentmapper;

	@Autowired
	private AgentaccountbankMapper agentaccountbankmapper;

	@Autowired
	private AgentaccountService agentaccountservice;

	@Autowired
	private SystemaccountService systemaccountservice;

	@Override
	@Transactional
	public Integer post(Agentaccountorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Agentaccountorder> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Agentaccountorder>(Collections.emptyList());
			}
		}
		List<Agentaccountorder> list = mapper.list(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
		});
		return new YtPageBean<Agentaccountorder>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccountorder get(Long id) {
		Agentaccountorder t = mapper.get(id);
		return t;
	}

	@Override
	public Integer save(Agentaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1，大于余额");
		}
		Agent m = null;
		if (t.getAgentid() == null) {
			m = agentmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = agentmapper.get(t.getAgentid());
			t.setUserid(m.getUserid());
		}
		Agentaccountbank mab = agentaccountbankmapper.get(Long.valueOf(t.getAccnumber()));
		t.setAccname(mab.getAccname());
		t.setAccnumber(mab.getAccnumber());
		// 插入提现记录
		t.setAgentid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNkname());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setExchange(t.getAgentexchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getAgentexchange());
		t.setType(DictionaryResource.ORDERTYPE_21.toString());
		t.setOrdernum("AW" + StringUtil.getOrderNum());
		t.setRemark("提现￥:" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		agentaccountservice.withdrawamount(t);
		//
		return i;
	}

////////////////////////////////////////////////// 提现 /////////////////////////
	@Override
	public void withdrawmanual(Agentaccountorder aco) {
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(aco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		Agentaccountorder mao = mapper.get(aco.getId());
		mao.setStatus(aco.getStatus());
		mao.setImgurl(aco.getImgurl());
		Integer i = mapper.put(mao);
		if (i > 0) {
			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
				agentaccountservice.updateWithdrawamount(mao);
				//
				systemaccountservice.updateAgentWithdrawamount(mao);
			} else {
				agentaccountservice.turndownWithdrawamount(mao);
			}
		}
	}

	@Override
	public Integer cancleWithdraw(Long id) {
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.PAYOUTSTATUS_51);
		Integer i = mapper.put(mao);
		agentaccountservice.cancleWithdrawamount(mao);
		return i;
	}

	@Override
	public Long incomewithdrawapp(Agentaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1，大于余额");
		}
		Agent m = null;
		if (t.getAgentid() == null) {
			m = agentmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = agentmapper.get(t.getAgentid());
			t.setUserid(m.getUserid());
		}
		// 插入提现记录
		t.setAgentid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNkname());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setExchange(t.getAgentexchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getAgentexchange());
		t.setType(DictionaryResource.ORDERTYPE_21.toString());
		t.setOrdernum("AW" + StringUtil.getOrderNum());
		t.setRemark("提现￥:" + String.format("%.2f", t.getAmountreceived()));
		mapper.post(t);

		// 支出账户和记录
		agentaccountservice.withdrawamount(t);
		//
		return t.getId();
	}

	@Override
	public Integer success(Long id) {
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		Integer i = mapper.put(mao);
		if (i > 0) {
			agentaccountservice.updateWithdrawamount(mao);
			systemaccountservice.updateAgentWithdrawamount(mao);
		}
		return i;
	}
}