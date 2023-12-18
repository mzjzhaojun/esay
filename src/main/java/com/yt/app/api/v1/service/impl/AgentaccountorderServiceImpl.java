package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountbankMapper;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.AgentaccountorderService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountbank;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

@Service
public class AgentaccountorderServiceImpl extends YtBaseServiceImpl<Agentaccountorder, Long>
		implements AgentaccountorderService {
	@Autowired
	private AgentaccountorderMapper mapper;

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
	public YtIPage<Agentaccountorder> list(Map<String, Object> param) {
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
	@Transactional
	public Integer save(Agentaccountorder t) {

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
		t.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		t.setExchange(t.getAgentexchange());
		t.setAmountreceived((t.getAmount()));
		t.setType(DictionaryResource.ORDERTYPE_21);
		t.setOrdernum("TXD" + StringUtil.getOrderNum());
		t.setRemark("提现金额：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		agentaccountservice.withdrawamount(t);
		//
		return i;
	}

//////////////////////////////////////////////////////////////收入
	@Override
	@Transactional
	public Integer pass(Long id) {
//
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
		Integer i = mapper.put(mao);
//
		agentaccountservice.updateTotalincome(mao);
//
		return i;
	}

	@Override
	@Transactional
	public Integer turndown(Long id) {
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
		Integer i = mapper.put(mao);
//
		agentaccountservice.turndownTotalincome(mao);
		return i;
	}

	@Override
	@Transactional
	public Integer cancle(Long id) {
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_13);
		Integer i = mapper.put(mao);
//
		agentaccountservice.cancleTotalincome(mao);
		return i;
	}

////////////////////////////////////////////////// 支出 /////////////////////////

	@Override
	@Transactional
	public Integer passWithdraw(Long id) {
//
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
		Integer i = mapper.put(mao);
//
		agentaccountservice.updateWithdrawamount(mao);
//
		systemaccountservice.updateWithdrawamount(mao);
//
		return i;
	}

	@Override
	@Transactional
	public Integer turndownWithdraw(Long id) {
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
		Integer i = mapper.put(mao);
//
		agentaccountservice.turndownWithdrawamount(mao);
		return i;
	}

	@Override
	@Transactional
	public Integer cancleWithdraw(Long id) {
		Agentaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_13);
		Integer i = mapper.put(mao);
//
		agentaccountservice.cancleWithdrawamount(mao);
		return i;
	}
}