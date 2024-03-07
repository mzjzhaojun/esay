package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountbankMapper;
import com.yt.app.api.v1.mapper.MerchantaccountorderMapper;
import com.yt.app.api.v1.service.MerchantaccountService;
import com.yt.app.api.v1.service.MerchantaccountorderService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.api.v1.entity.Merchantaccountorder;
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
 * @version v1 @createdate2023-11-15 09:44:15
 */

@Service
public class MerchantaccountorderServiceImpl extends YtBaseServiceImpl<Merchantaccountorder, Long>
		implements MerchantaccountorderService {
	@Autowired
	private MerchantaccountorderMapper mapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private MerchantaccountbankMapper merchantaccountbankmapper;

	@Autowired
	private MerchantaccountService merchantaccountservice;

	@Autowired
	private SystemaccountService systemaccountservice;

	// 收入
	@Override
	@Transactional
	public Integer post(Merchantaccountorder t) {

		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}
		// 收入订单
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		t.setAmountreceived((t.getAmount() * (t.getExchange() + t.getMerchantexchange())));
		t.setType(DictionaryResource.ORDERTYPE_20);
		t.setOrdernum("CZS" + StringUtil.getOrderNum());
		t.setRemark("充值金额：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 收入账户和记录
		merchantaccountservice.totalincome(t);
		//
		return i;
	}

	// 支出
	@Override
	@Transactional
	public Integer save(Merchantaccountorder t) {

		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}

		Merchantaccountbank mab = merchantaccountbankmapper.get(Long.valueOf(t.getAccnumber()));
		t.setAccname(mab.getAccname());
		t.setAccnumber(mab.getAccnumber());
		// 支出订单
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		t.setExchange(t.getMerchantexchange());
		t.setAmountreceived((t.getAmount()));
		t.setType(DictionaryResource.ORDERTYPE_22);
		t.setOrdernum("TXS" + StringUtil.getOrderNum());
		t.setRemark("提现金额：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		merchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	// 支出
	@Override
	@Transactional
	public Integer appsave(Merchantaccountorder t) {

		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}

		t.setAccname("app提现");

		// 支出订单
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		t.setExchange(t.getMerchantexchange());
		t.setAmountreceived((t.getAmount()));
		t.setType(DictionaryResource.ORDERTYPE_22);
		t.setOrdernum("TXS" + StringUtil.getOrderNum());
		t.setRemark("提现金额：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		merchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Merchantaccountorder> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Merchantaccountorder>(Collections.emptyList());
			}
		}
		List<Merchantaccountorder> list = mapper.list(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
		});
		return new YtPageBean<Merchantaccountorder>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchantaccountorder get(Long id) {
		Merchantaccountorder t = mapper.get(id);
		return t;
	}

	////////////////////////////////////////////////////////////// 收入
	@Override
	@Transactional
	public Integer pass(Long id) {
		//
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
		Integer i = mapper.put(mao);
		//
		merchantaccountservice.updateTotalincome(mao);
		//
		systemaccountservice.updateTotalincome(mao);
		//
		return i;
	}

	@Override
	@Transactional
	public Integer turndown(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
		Integer i = mapper.put(mao);
		//
		merchantaccountservice.turndownTotalincome(mao);
		return i;
	}

	@Override
	@Transactional
	public Integer cancle(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_13);
		Integer i = mapper.put(mao);
		//
		merchantaccountservice.cancleTotalincome(mao);
		return i;
	}

	////////////////////////////////////////////////// 支出 /////////////////////////

	@Override
	@Transactional
	public Integer passWithdraw(Long id) {
		//
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
		Integer i = mapper.put(mao);
		//
		merchantaccountservice.updateWithdrawamount(mao);
		//
		systemaccountservice.updateWithdrawamount(mao);
		//
		return i;
	}

	@Override
	@Transactional
	public Integer turndownWithdraw(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
		Integer i = mapper.put(mao);
		//
		merchantaccountservice.turndownWithdrawamount(mao);
		return i;
	}

	@Override
	@Transactional
	public Integer cancleWithdraw(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_13);
		Integer i = mapper.put(mao);
		//
		merchantaccountservice.cancleWithdrawamount(mao);
		return i;
	}
}