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
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountMapper;
import com.yt.app.api.v1.mapper.MerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.AgentService;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.MerchantaccountService;
import com.yt.app.api.v1.service.MerchantcustomerbanksService;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.Merchantbot;
import com.yt.app.api.v1.dbo.SysSubmitDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccount;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtCodeEnum;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.MyException;
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
 * @version v1 @createdate2023-11-15 09:51:11
 */

@Service
public class PayoutServiceImpl extends YtBaseServiceImpl<Payout, Long> implements PayoutService {
	@Autowired
	private PayoutMapper mapper;
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
	private MerchantaccountorderMapper merchantaccountordermapper;
	@Autowired
	private AgentMapper agentmapper;
	@Autowired
	private AgentaccountorderMapper agentaccountordermapper;
	@Autowired
	private ChannelaccountorderMapper channelaccountordermapper;
	@Autowired
	private MerchantaccountService merchantaccountservice;
	@Autowired
	private AgentaccountService agentaccountservice;
	@Autowired
	private ChannelaccountService channelaccountservice;
	@Autowired
	private SystemaccountService systemaccountservice;
	@Autowired
	private MerchantaccountMapper merchantaccountmapper;
	@Autowired
	private MerchantaisleMapper merchantaislemapper;
	@Autowired
	private Merchantbot mbot;
	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;
	@Autowired
	private MerchantcustomerbanksService merchantcustomerbanksservice;

	@Override
	@Transactional
	public Integer post(Payout t) {

		///////////////////////////////////////////////////// 录入代付订单/////////////////////////////////////////////////////
		Merchant m = merchantmapper.getByUserId(SysUserContext.getUserId());
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum(StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("PM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("商户代付新增￥:" + String.format("%.2f", t.getAmount()));
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有设置渠道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= t.getAmount() && c.getMin() <= t.getAmount())
				.collect(Collectors.toList());
		Assert.notEmpty(listcmm, "支付额度不匹配!");
		List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Channel cl = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Channel cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && t.getAmount().intValue() == Integer.parseInt(number)) {
						cl = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (t.getAmount().intValue() >= min && t.getAmount().intValue() <= max) {
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
			Assert.notNull(cl, "没有匹配的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelcost(cl.getOnecost());// 渠道手续费
			t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
			t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
			t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		}

		///////////////////////////////////////////////////// 计算商户订单
		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		Merchantaccountorder mao = new Merchantaccountorder();
		mao.setUserid(m.getUserid());
		mao.setMerchantid(m.getId());
		mao.setUsername(m.getName());
		mao.setNkname(m.getNikname());
		mao.setMerchantcode(m.getCode());
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		mao.setExchange(m.getExchange());
		mao.setAccname(t.getAccname());
		mao.setAccnumber(t.getAccnumer());
		mao.setDeal(t.getMerchantdeal());// 交易费
		mao.setOnecost(m.getOnecost());// 手续费
		mao.setAmount(t.getAmount());// 操作资金
		mao.setAmountreceived(t.getMerchantpay());// 总支付费用
		mao.setType(DictionaryResource.ORDERTYPE_23);
		mao.setOrdernum(t.getMerchantordernum());
		mao.setRemark("商户代付操作资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费："
				+ m.getOnecost());
		merchantaccountordermapper.post(mao);
		merchantaccountservice.payout(mao);
		///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
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
			aat.setAmount(t.getMerchantdeal());// 金额
			aat.setDeal(t.getMerchantdeal() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
			aat.setOnecost(ag.getOnecost());// 手续费
			aat.setType(DictionaryResource.ORDERTYPE_23);
			aat.setOrdernum("PA" + StringUtil.getOrderNum());
			aat.setRemark("代付资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费："
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
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Payout> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Payout>(Collections.emptyList());
			}
		}
		List<Payout> list = mapper.list(param);
		return new YtPageBean<Payout>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Payout get(Long id) {
		Payout t = mapper.get(id);
		return t;
	}

	@Override
	public SysResultVO query(String merchantordernum) {
		Payout pt = mapper.getByMerchantOrdernum(merchantordernum);
		if (pt == null) {
			new MyException("订单不存在!", YtCodeEnum.YT888);
		}
		SysResultVO srv = new SysResultVO();
		srv.setBankcode(pt.getBankcode());
		srv.setCode(pt.getStatus());
		srv.setMerchantid(pt.getMerchantcode());
		srv.setMerchantorderid(merchantordernum);
		srv.setPayamt(pt.getAmount());
		return srv;
	}

	@Override
	public YtBody tycallbackpay(SysTyOrder so) {
		RLock lock = RedissonUtil.getLock(so.getTypay_order_id());
		try {
			lock.lock();
			Payout pt = mapper.getByMerchantOrdernum(so.getTypay_order_id());
			if (pt != null) {
				SysUserContext.setUserId(pt.getUserid());
				TenantIdContext.setTenantId(pt.getTenant_id());
				if (pt.getStatus().equals(DictionaryResource.PAYOUTSTATUS_51)) {
					Channel cl = channelmapper.get(pt.getChannelid());
					// md5值是否被篡改
					if (PayUtil.valMd5TyOrder(so, cl.getApikey())) {
						if (so.getPay_message() == 1) {
							paySuccess(pt);
						} else {
							payFail(pt);
						}
					}
				}
			} else {
				SysUserContext.remove();
				TenantIdContext.remove();
				return new YtBody("失败", 100);
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		SysUserContext.remove();
		TenantIdContext.remove();
		return new YtBody("成功", 200);
	}

	// 盘口提交订单
	@Override
	public SysResultVO submit(SysSubmitDTO ss) {
		String code = ss.getMerchantid();
		Merchant mc = merchantmapper.getByCode(code.toString());
		if (mc == null) {
			new MyException("商户不存在!", YtCodeEnum.YT888);
		}

		Merchantaccount ma = merchantaccountmapper.getByUserId(mc.getUserid());
		if (ma.getBalance() < ss.getPayamt()) {
			new MyException("余额不足!", YtCodeEnum.YT888);
		}

		Boolean val = PayUtil.valMd5Submit(ss, mc.getAppkey());
		if (!val) {
			new MyException("签名不正确!", YtCodeEnum.YT888);
		}

		List<Merchantaisle> listmc = merchantaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
			new MyException("商戶沒有配置通道!", YtCodeEnum.YT888);
		}
		// 下單
		Payout pt = new Payout();
		pt.setAccname(ss.getBankowner());
		pt.setAccnumer(ss.getBanknum());
		pt.setBankcode(ss.getBankcode());
		pt.setBankaddress(ss.getBankaddress());
		pt.setAmount(ss.getPayamt());
		pt.setNotifyurl(mc.getApireusultip());
		pt.setAisleid(listmc.get(0).getAisleid());
		pt.setBankname(ss.getBankname());
		pt.setMerchantorderid(ss.getMerchantorderid());
		add(pt, mc);
		// 返回
		SysResultVO sr = new SysResultVO();
		sr.setBankcode(ss.getBankcode());
		sr.setMerchantid(sr.getMerchantid());
		sr.setMerchantorderid(ss.getMerchantorderid());
		sr.setPayamt(ss.getPayamt());
		sr.setPayorderid(pt.getMerchantordernum());
		sr.setRemark(ss.getRemark());
		sr.setPaytype(ss.getPaytype());
		sr.setSign(PayUtil.Md5Result(sr, mc.getAppkey()));
		// 返回给盘口订单号
		return sr;
	}

	@Transactional
	public Integer add(Payout t, Merchant m) {
		TenantIdContext.setTenantId(m.getTenant_id());
		///////////////////////////////////////////////////// 盘口录入代付订单/////////////////////////////////////////////////////
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum(StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("PM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61); // 盘口发起
		t.setRemark("盘口代付新增￥:" + String.format("%.2f", t.getAmount()));
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有设置渠道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= t.getAmount() && c.getMin() <= t.getAmount())
				.collect(Collectors.toList());
		Assert.notEmpty(listcmm, "支付额度不匹配!");
		List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Channel cl = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Channel cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && t.getAmount().intValue() == Integer.parseInt(number)) {
						cl = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (t.getAmount().intValue() >= min && t.getAmount().intValue() <= max) {
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
			Assert.notNull(cl, "没有匹配的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelcost(cl.getOnecost());// 渠道手续费
			t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
			t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
			t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		}

		///////////////////////////////////////////////////// 计算商户订单/////////////////////////////////////////////////////
		Merchantaccountorder mao = new Merchantaccountorder();
		mao.setUserid(m.getUserid());
		mao.setMerchantid(m.getId());
		mao.setUsername(m.getName());
		mao.setNkname(m.getNikname());
		mao.setMerchantcode(m.getCode());
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		mao.setExchange(m.getExchange());
		mao.setAccname(t.getAccname());
		mao.setAccnumber(t.getAccnumer());
		mao.setDeal(t.getMerchantdeal());// 交易费
		mao.setOnecost(m.getOnecost());// 手续费
		mao.setAmount(t.getAmount());// 操作资金
		mao.setAmountreceived(t.getMerchantpay());// 总支付费用
		mao.setType(DictionaryResource.ORDERTYPE_23);
		mao.setOrdernum(t.getMerchantordernum());
		mao.setRemark("盘口代付操作资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费："
				+ m.getOnecost());
		merchantaccountordermapper.post(mao);
		merchantaccountservice.payout(mao);

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
			aat.setAmount(t.getMerchantdeal());// 金额
			aat.setDeal(t.getMerchantdeal() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
			aat.setOnecost(ag.getOnecost());// 手续费
			aat.setType(DictionaryResource.ORDERTYPE_23);
			aat.setOrdernum("PA" + StringUtil.getOrderNum());
			aat.setRemark("盘口代付资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费："
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
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		//
		Integer i = mapper.post(t);
		TenantIdContext.remove();
		return i;
	}

	@Override
	public YtIPage<PayoutVO> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<PayoutVO>(Collections.emptyList());
			}
		}
		List<PayoutVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setNotifystatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getNotifystatus()));
		});
		return new YtPageBean<PayoutVO>(param, list, count);
	}

	/***
	 * 手动成功
	 */
	@Transactional
	public void paySuccess(Payout pt) {
		RLock lock = RedissonUtil.getLock(pt.getId());
		try {
			lock.lock();
			Payout t = mapper.get(pt.getId());
			if (t.getStatus().equals(DictionaryResource.PAYOUTSTATUS_51)) {
				// 计算商户订单/////////////////////////////////////////////////////
				Merchantaccountorder mao = merchantaccountordermapper.getByOrdernum(t.getMerchantordernum());
				mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
				// 商户订单
				merchantaccountordermapper.put(mao);
				// 商户账户
				merchantaccountservice.updatePayout(mao);
				// 系统账户
				systemaccountservice.updatePayout(mao);

				// 计算商户数据
				merchantservice.updatePayout(t);

				// 计算代理
				if (t.getAgentid() != null) {
					Agentaccountorder aao = agentaccountordermapper.getByOrdernum(t.getAgentordernum());
					aao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
					// 代理订单
					agentaccountordermapper.put(aao);
					// 代理账户
					agentaccountservice.updateTotalincome(aao);
					// 计算代理数据
					agentservice.updatePayout(t);
				}

				// 计算渠道
				Channelaccountorder cao = channelaccountordermapper.getByOrdernum(t.getChannelordernum());
				cao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
				// 渠道订单
				channelaccountordermapper.put(cao);
				// 渠道账户
				channelaccountservice.updateWithdrawamount(cao);
				// 计算渠道数据
				channelservice.updatePayout(t);

				// ------------------更新代付订单-----------------
				t.setStatus(DictionaryResource.PAYOUTSTATUS_52);
				if (t.getNotifystatus().equals(DictionaryResource.PAYOUTNOTIFYSTATUS_61)) {
					t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
				}
				t.setRemark("代付成功￥" + pt.getAmount());
				t.setSuccesstime(DateTimeUtil.getNow());
				t.setBacklong(DateUtil.between(t.getSuccesstime(), t.getCreate_time(), DateUnit.SECOND));

				t.setImgurl(pt.getImgurl());
				//
				int i = mapper.put(t);
				if (i > 0) {
					Tgmerchantgroup tgmerchantgroup = tgmerchantgroupmapper.getByMerchantId(t.getMerchantid());
					StringBuffer what = new StringBuffer();
					what.append("状态：代付成功\n");
					what.append("单号：" + t.getMerchantordernum() + "\n");
					what.append("姓名：" + t.getAccname() + "\n");
					what.append("卡号：" + t.getAccnumer() + "\n");
					what.append("金额：" + t.getAmount() + "\n");
					what.append("成功时间：" + DateTimeUtil.getDateTime() + "\n");
					what.append("业务部已处理完毕，请你们核实查看\n");
					if (tgmerchantgroup != null)
						mbot.sendText(tgmerchantgroup.getTgid(), what.toString());
				}
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
			} else {
				new MyException("已经处理完成，不要重复处理", YtCodeEnum.YT888);
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 
	 * 手动回调失败
	 * 
	 */
	@Transactional
	public void payFail(Payout t) {
		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			if (t.getStatus().equals(DictionaryResource.PAYOUTSTATUS_51)) {
				// 计算商户订单/////////////////////////////////////////////////////
				Merchantaccountorder mao = merchantaccountordermapper.getByOrdernum(t.getMerchantordernum());
				mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
				merchantaccountordermapper.put(mao);
				//
				merchantaccountservice.turndownPayout(mao);

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
				channelaccountservice.turndownWithdrawamount(cao);

				//
				t.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				if (t.getNotifystatus().equals(DictionaryResource.PAYOUTNOTIFYSTATUS_61)) {
					t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
				}
				t.setRemark("代付失败￥" + t.getAmount());
				t.setSuccesstime(DateTimeUtil.getNow());
				t.setBacklong(DateTimeUtil.diffDays(t.getSuccesstime(), t.getCreate_time()));
				int i = mapper.put(t);
				if (i > 0) {
					Tgmerchantgroup tgmerchantgroup = tgmerchantgroupmapper.getByMerchantId(t.getMerchantid());
					StringBuffer what = new StringBuffer();
					what.append("状态：代付失败\n");
					what.append("单号：" + t.getMerchantordernum() + "\n");
					what.append("姓名：" + t.getAccname() + "\n");
					what.append("卡号：" + t.getAccnumer() + "\n");
					what.append("金额：" + t.getAmount() + "\n");
					what.append("失败时间：" + DateTimeUtil.getDateTime() + "\n");
					what.append("业务部已处理完毕，请你们核实查看\n");
					if (tgmerchantgroup != null)
						mbot.sendText(tgmerchantgroup.getTgid(), what.toString());
				}
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
			} else {
				new MyException("已经处理完成，不要重复处理", YtCodeEnum.YT888);
			}

		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void payoutmanual(Payout pt) {
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(pt.getRemark()),
				System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		if (pt.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
			paySuccess(pt);
		} else {
			payFail(pt);
		}
	}

}