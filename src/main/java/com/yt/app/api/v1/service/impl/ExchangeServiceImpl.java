package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.AislechannelMapper;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountorderMapper;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.mapper.ExchangeMerchantaccountMapper;
import com.yt.app.api.v1.mapper.ExchangeMerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.AgentService;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.ExchangeMerchantaccountService;
import com.yt.app.api.v1.service.ExchangeService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.MerchantcustomerbanksService;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.MerchantMsgBot;
import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.ExchangeVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */

@Service
public class ExchangeServiceImpl extends YtBaseServiceImpl<Exchange, Long> implements ExchangeService {
	@Autowired
	private ExchangeMapper mapper;
	@Autowired
	private UserMapper usermapper;
	@Autowired
	private MerchantMapper merchantmapper;
	@Autowired
	private AisleMapper aislemapper;
	@Autowired
	private ChannelMapper channelmapper;
	@Autowired
	private AislechannelMapper aislechannelmapper;
	@Autowired
	private AgentService agentservice;
	@Autowired
	private ChannelService channelservice;
	@Autowired
	private MerchantService merchantservice;
	@Autowired
	private ExchangeMerchantaccountorderMapper exchangemerchantaccountordermapper;
	@Autowired
	private AgentMapper agentmapper;
	@Autowired
	private AgentaccountorderMapper agentaccountordermapper;
	@Autowired
	private ChannelaccountorderMapper channelaccountordermapper;
	@Autowired
	private ExchangeMerchantaccountService exchangemerchantaccountservice;
	@Autowired
	private AgentaccountService agentaccountservice;
	@Autowired
	private ChannelaccountService channelaccountservice;
	@Autowired
	private SystemaccountService systemaccountservice;
	@Autowired
	private ExchangeMerchantaccountMapper exchangemerchantaccountmapper;
	@Autowired
	private MerchantaisleMapper merchantaislemapper;
	@Autowired
	private MerchantMsgBot mbot;
	@Autowired
	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;
	@Autowired
	private MerchantcustomerbanksService merchantcustomerbanksservice;
	@Autowired
	private PayconfigService payconfigservice;

	@Override
	@Transactional
	public Integer post(Exchange t) {
		ExchangeMerchantaccount ma = exchangemerchantaccountmapper.getByUserId(SysUserContext.getUserId());

		///////////////////////////////////////////////////// 录入换汇订单/////////////////////////////////////////////////////
		Merchant m = merchantmapper.getByUserId(SysUserContext.getUserId());
		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}

		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum(StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("EM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantrealtimeexchange(t.getExchange());
		t.setMerchantdowpoint(m.getExchangedownpoint());
		t.setMerchantonecost(m.getExchangeonecost());
		t.setMerchantpay((t.getAmount() / (t.getExchange() + m.getExchangedownpoint())) + t.getMerchantonecost());// 商户usdt支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark(
				"换汇新增￥：" + String.format("%.2f", t.getAmount()) + ",USDT" + String.format("%.2f", t.getMerchantpay()));

		if (t.getMerchantpay() <= 0 || t.getMerchantpay() > ma.getBalance()) {
			throw new YtException("账户余额不足");
		}

		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<Channel> listcmm = listc.stream()
				.filter(c -> c.getMax() >= t.getMerchantpay() && c.getMin() <= t.getMerchantpay())
				.collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代付金额超出限额");
		List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Channel cl = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Channel cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && t.getMerchantpay().intValue() == Integer.parseInt(number)) {
						cl = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (t.getMerchantpay().intValue() >= min && t.getMerchantpay().intValue() <= max) {
							cl = cc;
						}
					}
				}
			}
		} else {
			List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
			double count = 0;
			for (Channel cml : listcmm) {
				count = count + cml.getWeight();
			}
			for (Channel cmm : listcmm) {
				weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
			}
			WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
			String code = wr.next();
			cl = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
			Assert.notNull(cl, "没有可用的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelonecost(cl.getExchangeonecost());
			t.setChannelrealtimeexchange(t.getExchange() + cl.getExchangedownpoint());// 渠道手续费
			t.setChanneldowpoint(cl.getExchangedownpoint());
			t.setChannelpay(t.getAmount() / t.getChannelrealtimeexchange());// 渠道总支付usdt
			t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		}

		///////////////////////////////////////////////////// 计算商户订单
		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		ExchangeMerchantaccountorder mao = new ExchangeMerchantaccountorder();
		mao.setUserid(m.getUserid());
		mao.setMerchantid(m.getId());
		mao.setUsername(m.getName());
		mao.setNkname(m.getNikname());
		mao.setMerchantcode(m.getCode());
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		mao.setExchange(t.getExchange() + m.getExchangedownpoint());
		mao.setMerchantexchange(m.getExchange());
		mao.setAccname(t.getAccname());
		mao.setAccnumber(t.getAccnumer());
		mao.setRealtimeexchange(t.getExchange());
		mao.setDowpoint(m.getExchangedownpoint());
		mao.setAmount(t.getAmount());// 操作资金
		mao.setAmountreceived(t.getMerchantpay());// 总支付费用
		mao.setType(DictionaryResource.ORDERTYPE_22);
		mao.setOrdernum(t.getMerchantordernum());
		mao.setUsdtval(t.getMerchantpay());
		mao.setRemark("换汇操作资金：" + t.getAmount() + " usdt：" + String.format("%.2f", t.getMerchantpay()) + " 手续费："
				+ m.getExchangeonecost());
		exchangemerchantaccountordermapper.post(mao);
		exchangemerchantaccountservice.exchange(mao);

		///////////////////////////////////////////////////// 計算代理訂單/////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			Agentaccountorder aat = new Agentaccountorder();
			aat.setAgentid(ag.getId());
			aat.setUserid(ag.getUserid());
			aat.setUsername(ag.getName());
			aat.setNkname(ag.getNkname());
			aat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
			aat.setExchange(ag.getExchange());
			aat.setAccname(t.getAccname());
			aat.setAccnumber(t.getAccnumer());
			aat.setAmount(t.getMerchantpay());// 金额
			aat.setDeal(t.getMerchantpay() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
			aat.setOnecost(ag.getOnecost());// 手续费
			aat.setType(DictionaryResource.ORDERTYPE_22);
			aat.setOrdernum("EA" + StringUtil.getOrderNum());
			aat.setRemark("换汇资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费："
					+ aat.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance());
		t.setExchange(t.getMerchantrealtimeexchange() + t.getMerchantdowpoint());
		//
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Exchange> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Exchange>(Collections.emptyList());
			}
		}
		List<Exchange> list = mapper.list(param);
		return new YtPageBean<Exchange>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Exchange get(Long id) {
		Exchange t = mapper.get(id);
		return t;
	}

	@Override
	public Exchange query(String channelordernum) {
		Exchange pt = mapper.getByChannelOrdernum(channelordernum);
		return pt;
	}

	@Override
	public YtBody tycallbackpay(SysTyOrder so) {
		RLock lock = RedissonUtil.getLock(so.getTypay_order_id());
		try {
			lock.lock();
			Exchange pt = mapper.getByChannelOrdernum(so.getTypay_order_id());
			if (pt == null)
				return new YtBody("失败", 100);
			if (pt.getStatus().equals(DictionaryResource.PAYOUTSTATUS_51)) {
				Channel cl = channelmapper.get(pt.getChannelid());
				// md5值是否被篡改
				if (PayUtil.valMd5TyResultOrder(so, cl.getApikey())) {
					if (so.getPay_message() == 1) {
						paySuccess(pt);
						return new YtBody("成功", 200);
					} else if (so.getPay_message() == -2) {
						payFail(pt);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		return new YtBody("成功", 200);
	}

	// API提交订单
	@Override
	public Exchange submit(PaySubmitDTO ss) {
		String id = ss.getMerchantid();
		Merchant mc = merchantmapper.get(Long.valueOf(id));
		Assert.notNull(mc, "商户不存在!");

		List<Merchantaisle> listmc = merchantaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
			throw new YtException("商戶沒有配置通道!");
		}

		Payconfig pc = payconfigservice.getData();

		Exchange pt = new Exchange();
		pt.setAccname(ss.getBankowner());
		pt.setAccnumer(ss.getBanknum());
		pt.setBankcode(ss.getBankcode());
		pt.setBankaddress(ss.getBankaddress());
		pt.setAmount(ss.getPayamt());
		pt.setNotifyurl(mc.getApireusultip());
		pt.setAisleid(listmc.get(0).getAisleid());
		pt.setBankname(ss.getBankname());
		pt.setQrcode(ss.getQrcode());
		pt.setExchange(pc.getExchange());
		addExchange(pt, mc);
		return pt;
	}

	@Transactional
	public void addExchange(Exchange t, Merchant m) {

		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		ExchangeMerchantaccount ma = exchangemerchantaccountmapper.getByUserId(m.getUserid());

		TenantIdContext.setTenantId(m.getTenant_id());
		///////////////////////////////////////////////////// 盘口录入代付订单/////////////////////////////////////////////////////
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum(StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("EM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantrealtimeexchange(t.getExchange());
		t.setMerchantdowpoint(m.getExchangedownpoint());
		t.setMerchantonecost(m.getExchangeonecost());
		t.setMerchantpay((t.getAmount() / (t.getExchange() + m.getExchangedownpoint())) + t.getMerchantonecost());// 商户usdt支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61); // 盘口发起
		t.setRemark("群内换汇新增￥：" + String.format("%.2f", t.getAmount()) + ",USDT"
				+ String.format("%.2f", t.getMerchantpay()));

		if (ma.getBalance() < t.getMerchantpay() || t.getMerchantpay() <= 0) {
			throw new YtException("账户余额不足!");
		}

		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<Channel> listcmm = listc.stream()
				.filter(c -> c.getMax() >= t.getMerchantpay() && c.getMin() <= t.getMerchantpay())
				.collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代付金额超出限额");
		List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Channel cl = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Channel cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && t.getMerchantpay().intValue() == Integer.parseInt(number)) {
						cl = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (t.getMerchantpay().intValue() >= min && t.getMerchantpay().intValue() <= max) {
							cl = cc;
						}
					}
				}
			}
		} else {
			List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
			double count = 0;
			for (Channel cml : listcmm) {
				count = count + cml.getWeight();
			}
			for (Channel cmm : listcmm) {
				weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
			}
			WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
			String code = wr.next();
			cl = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
			Assert.notNull(cl, "没有可用的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelonecost(cl.getExchangeonecost());
			t.setChannelrealtimeexchange(t.getExchange() + cl.getExchangedownpoint());// 渠道手续费
			t.setChanneldowpoint(cl.getExchangedownpoint());
			t.setChannelpay(t.getAmount() / t.getChannelrealtimeexchange());// 渠道总支付usdt
			t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		}

		///////////////////////////////////////////////////// 计算商户订单
		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		ExchangeMerchantaccountorder mao = new ExchangeMerchantaccountorder();
		mao.setUserid(m.getUserid());
		mao.setMerchantid(m.getId());
		mao.setUsername(m.getName());
		mao.setNkname(m.getNikname());
		mao.setMerchantcode(m.getCode());
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		mao.setExchange(t.getExchange() + m.getExchangedownpoint());
		mao.setMerchantexchange(m.getExchange());
		mao.setAccname(t.getAccname());
		mao.setAccnumber(t.getAccnumer());
		mao.setRealtimeexchange(t.getExchange());
		mao.setDowpoint(m.getExchangedownpoint());
		mao.setAmount(t.getAmount());// 操作资金
		mao.setAmountreceived(t.getMerchantpay());// 总支付费用
		mao.setType(DictionaryResource.ORDERTYPE_22);
		mao.setOrdernum(t.getMerchantordernum());
		mao.setRemark("群内换汇操作资金：" + t.getAmount() + " usdt：" + String.format("%.2f", t.getMerchantpay()) + " 手续费："
				+ m.getExchangeonecost());
		exchangemerchantaccountordermapper.post(mao);
		exchangemerchantaccountservice.exchange(mao);

		///////////////////////////////////////////////////// 计算代理订单
		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			Agentaccountorder aat = new Agentaccountorder();
			aat.setAgentid(ag.getId());
			aat.setUserid(ag.getUserid());
			aat.setUsername(ag.getName());
			aat.setNkname(ag.getNkname());
			aat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
			aat.setExchange(ag.getExchange());
			aat.setAccname(t.getAccname());
			aat.setAccnumber(t.getAccnumer());
			aat.setAmount(t.getMerchantpay());// 金额
			aat.setDeal(t.getMerchantpay() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
			aat.setOnecost(ag.getOnecost());// 手续费
			aat.setType(DictionaryResource.ORDERTYPE_22);
			aat.setOrdernum("EA" + StringUtil.getOrderNum());
			aat.setRemark("群内换汇资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费："
					+ aat.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance());
		t.setExchange(t.getMerchantrealtimeexchange() + t.getMerchantdowpoint());
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		//
		mapper.post(t);
		TenantIdContext.remove();
	}

	@Override
	public YtIPage<ExchangeVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<ExchangeVO>(Collections.emptyList());
		}
		List<ExchangeVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<ExchangeVO>(param, list, count);
	}

	@Transactional
	public void paySuccess(Exchange pt) {
		RLock lock = RedissonUtil.getLock(pt.getId());
		try {
			lock.lock();
			Exchange t = mapper.get(pt.getId());
			// 计算商户订单/////////////////////////////////////////////////////
			ExchangeMerchantaccountorder mao = exchangemerchantaccountordermapper
					.getByOrdernum(t.getMerchantordernum());
			mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
			// 商户订单
			exchangemerchantaccountordermapper.put(mao);
			// 商户账户
			exchangemerchantaccountservice.updateExchange(mao);
			// 系统账户
			systemaccountservice.updateExchange(mao);

			// 计算商户数据
			merchantservice.updateExchange(t);

			// 计算代理
			if (t.getAgentid() != null) {
				Agentaccountorder aao = agentaccountordermapper.getByOrdernum(t.getAgentordernum());
				aao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
				// 代理订单
				agentaccountordermapper.put(aao);
				// 代理账户
				agentaccountservice.updateTotalincome(aao);
				// 计算代理数据
				agentservice.updateExchange(t);
			}

			// 计算渠道
			Channelaccountorder cao = channelaccountordermapper.getByOrdernum(t.getChannelordernum());
			cao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
			// 渠道订单
			channelaccountordermapper.put(cao);
			// 渠道账户
			channelaccountservice.updateexchangeamount(cao);
			// 计算渠道数据
			channelservice.updateExchange(t);

			// ------------------更新代付订单-----------------
			t.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			t.setRemark("换汇成功￥:" + pt.getAmount());
			t.setSuccesstime(DateTimeUtil.getNow());
			t.setBacklong(DateUtil.between(t.getSuccesstime(), t.getCreate_time(), DateUnit.SECOND));

			t.setImgurl(pt.getImgurl());
			//
			int i = mapper.put(t);
			if (i > 0) {
				Tgmerchantchannelmsg tgmerchantchannelmsg = tgmerchantchannelmsgmapper.getOrderNum(t.getOrdernum());
				StringBuffer what = new StringBuffer();
				String strnum = t.getAccnumer();
				if (strnum == null)
					strnum = "收款码";
				what.append("状态：换汇成功\n");
				what.append("单号：" + t.getMerchantordernum() + "\n");
				what.append("姓名：" + t.getAccname() + "\n");
				what.append("卡号：" + strnum + "\n");
				what.append("金额：" + t.getAmount() + "\n");
				what.append("成功时间：" + DateTimeUtil.getDateTime() + "\n");
				what.append("兑换部已处理完毕，请你们核实查看\n");
				if (tgmerchantchannelmsg != null)
					mbot.sendReplyText(tgmerchantchannelmsg.getChatid(), tgmerchantchannelmsg.getCreplyid(),
							what.toString());
			}
			// 保存客户信息
			merchantcustomerbanksservice.add(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 
	 * 回调失败
	 * 
	 */
	@Transactional
	public void payFail(Exchange t) {
		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			// 计算商户订单/////////////////////////////////////////////////////
			ExchangeMerchantaccountorder mao = exchangemerchantaccountordermapper
					.getByOrdernum(t.getMerchantordernum());
			mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
			exchangemerchantaccountordermapper.put(mao);
			//
			exchangemerchantaccountservice.turndownExchange(mao);

			// 计算代理
			if (t.getAgentid() != null) {
				Agentaccountorder aao = agentaccountordermapper.getByOrdernum(t.getAgentordernum());
				aao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
				agentaccountordermapper.put(aao);
				//
				agentaccountservice.turndownTotalincome(aao);
			}

			// 计算渠道
			Channelaccountorder cao = channelaccountordermapper.getByOrdernum(t.getChannelordernum());
			cao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
			channelaccountordermapper.put(cao);
			//
			channelaccountservice.turndownexchangeamount(cao);

			//
			t.setStatus(DictionaryResource.PAYOUTSTATUS_53);
			t.setRemark("换汇成功￥:" + t.getAmount());
			t.setSuccesstime(DateTimeUtil.getNow());
			t.setBacklong(DateTimeUtil.diffDays(t.getSuccesstime(), t.getCreate_time()));
			t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			int i = mapper.put(t);
			if (i > 0) {
				Tgmerchantchannelmsg tgmerchantchannelmsg = tgmerchantchannelmsgmapper.getOrderNum(t.getOrdernum());
				StringBuffer what = new StringBuffer();
				String strnum = t.getAccnumer();
				if (strnum == null)
					strnum = "收款码";
				what.append("状态：换汇失败\n");
				what.append("单号：" + t.getMerchantordernum() + "\n");
				what.append("姓名：" + t.getAccname() + "\n");
				what.append("卡号：" + strnum + "\n");
				what.append("金额：" + t.getAmount() + "\n");
				what.append("失败时间：" + DateTimeUtil.getDateTime() + "\n");
				what.append("兑换部已处理完毕，请你们核实\n");
				if (tgmerchantchannelmsg != null)
					mbot.sendReplyText(tgmerchantchannelmsg.getChatid(), tgmerchantchannelmsg.getCreplyid(),
							what.toString());
			}
			// 保存客户信息
			merchantcustomerbanksservice.add(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void exchangemanual(Exchange ex) {
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(ex.getRemark()),
				System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		if (ex.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
			paySuccess(ex);
		} else {
			payFail(ex);
		}
	}

	@Override
	public Exchange submit(String ordernum) {
		Exchange ex = mapper.getByOrdernum(ordernum);
		if (ex.getStatus().equals(DictionaryResource.PAYOUTSTATUS_51))
			paySuccess(ex);
		return ex;
	}
}