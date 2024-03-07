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
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountMapper;
import com.yt.app.api.v1.mapper.MerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.service.AgentService;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.ExchangeService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.MerchantaccountService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.dbo.SysSubmitDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccount;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.api.v1.vo.ExchangeVO;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtCodeEnum;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.MyException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.NotifyExchangeThread;
import com.yt.app.common.runnable.TaskExecutor;
import com.yt.app.common.util.DateTimeUtil;
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

	@Override
	@Transactional
	public Integer post(Exchange t) {

		///////////////////////////////////////////////////// 录入代付订单/////////////////////////////////////////////////////
		Merchant m = merchantmapper.getByUserId(SysUserContext.getUserId());
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum(StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("DFS" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增换汇");
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		////////////////////////////////////////////////////// ////////////////////////////////////////////////////////////
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

		///////////////////////////////////////////////////// channelcordernum/////////////////////////////////////////////////////
		String channelcordernum = channelservice.getChannelOrder(t, cl);
		Assert.notNull(channelcordernum, "通道单号获取失败!");
		t.setChannelordernum(channelcordernum);

		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		Merchantaccountorder mao = new Merchantaccountorder();
		mao.setUserid(m.getUserid());
		mao.setMerchantid(m.getId());
		mao.setUsername(m.getName());
		mao.setNkname(m.getNikname());
		mao.setMerchantcode(m.getCode());
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		mao.setExchange(m.getExchange());
		mao.setAmount(t.getMerchantdeal());// 交易费用
		mao.setAmountreceived(t.getMerchantpay());// 总支付费用
		mao.setType(DictionaryResource.ORDERTYPE_22);
		mao.setOrdernum(t.getMerchantordernum());
		mao.setRemark("操作资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费："
				+ m.getOnecost());
		merchantaccountordermapper.post(mao);
		//
		merchantaccountservice.payout(mao);

		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			Agentaccountorder aat = new Agentaccountorder();
			//
			aat.setAgentid(ag.getId());
			aat.setUserid(ag.getUserid());
			aat.setUsername(ag.getName());
			aat.setNkname(ag.getNkname());
			aat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
			aat.setExchange(ag.getExchange());
			aat.setAmount(t.getMerchantdeal() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getAmount() + ag.getOnecost());// 总费用
			aat.setType(DictionaryResource.ORDERTYPE_20);
			aat.setOrdernum("DFA" + StringUtil.getOrderNum());
			aat.setRemark("操作资金：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getAmount()) + " 手续费："
					+ ag.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			//
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		Channel cll = channelmapper.get(t.getChannelid());
		Channelaccountorder cat = new Channelaccountorder();
		cat.setUserid(cll.getUserid());
		//
		cat.setChannelid(cll.getId());
		cat.setUsername(cll.getName());
		cat.setNkname(cll.getNkname());
		cat.setChannelcode(cll.getCode());
		cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		cat.setAmount(t.getChanneldeal());
		cat.setExchange(cll.getExchange());
		cat.setChannelexchange(cll.getExchange());
		cat.setAmountreceived(t.getChannelpay());
		cat.setType(DictionaryResource.ORDERTYPE_22);
		cat.setOrdernum(t.getChannelordernum());
		cat.setRemark(
				"资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", cat.getAmount()) + " 手续费：" + cll.getOnecost());
		channelaccountordermapper.post(cat);
		//
		channelaccountservice.withdrawamount(cat);
		// 渠道余额
		t.setChannelbalance(cl.getBalance());
		//
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		//
		Integer i = mapper.post(t);
		return i;
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
		Exchange pt = mapper.getByChannelOrdernum(so.getTypay_order_id());
		if (pt == null)
			return new YtBody("失败", 100);
		RLock lock = RedissonUtil.getLock(pt.getId());
		try {
			lock.lock();
			if (pt.getStatus() != DictionaryResource.PAYOUTSTATUS_52) {
				Channel cl = channelmapper.get(pt.getChannelid());
				// md5值是否被篡改
				if (PayUtil.valMd5TyOrder(so, cl.getApikey())) {
					if (so.getPay_message() == 1) {
						paySuccess(pt);
						// 执行通知
						if (pt.getNotifystatus() == DictionaryResource.PAYOUTNOTIFYSTATUS_61) {
							NotifyExchangeThread notifythread = new NotifyExchangeThread(mapper, merchantmapper, pt,
									200);
							TaskExecutor.tastpool.execute(notifythread);
						}
						return new YtBody("成功", 200);
					} else if (so.getPay_message() == -2) {
						if (pt.getNotifystatus() == DictionaryResource.PAYOUTNOTIFYSTATUS_61) {
							NotifyExchangeThread notifythread = new NotifyExchangeThread(mapper, merchantmapper, pt,
									100);
							TaskExecutor.tastpool.execute(notifythread);
						}
						payFail(pt);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		return new YtBody("失败", 100);
	}

	// 盘口提交订单
	@Override
	public SysResultVO submit(SysSubmitDTO ss) {
		Integer code = ss.getMerchantid();
		Merchant mc = merchantmapper.getByCode(code.toString());
		Assert.notNull(mc, "商户不存在!");

		Merchantaccount ma = merchantaccountmapper.getByUserId(mc.getUserid());
		if (ma.getBalance() < ss.getPayamt()) {
			new MyException("余额不足", YtCodeEnum.YT888);
		}

		Boolean val = PayUtil.valMd5Submit(ss, mc.getAppkey());
		Assert.isTrue(val, "签名不正确!");

		List<Merchantaisle> listmc = merchantaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
			new MyException("商戶沒有配置通道!", YtCodeEnum.YT888);
		}

		Exchange pt = new Exchange();
		pt.setAccname(ss.getBankowner());
		pt.setAccnumer(ss.getBanknum());
		pt.setBankcode(ss.getBankcode());
		pt.setBankaddress(ss.getBankaddress());
		pt.setAmount(ss.getPayamt());
		pt.setNotifyurl(mc.getApireusultip());
		pt.setAisleid(listmc.get(0).getAisleid());
		pt.setBankname(ss.getBankname());
		add(pt, mc);

		SysResultVO sr = new SysResultVO();
		sr.setBankcode(ss.getBankcode());
		sr.setMerchantid(sr.getMerchantid());
		sr.setMerchantorderid(ss.getMerchantorderid());
		sr.setPayamt(ss.getPayamt());
		sr.setPayorderid(pt.getChannelordernum());
		sr.setRemark(ss.getRemark());
		sr.setPaytype(ss.getPaytype());
		sr.setSign(PayUtil.Md5Result(sr, mc.getAppkey()));

		return sr;
	}

	@Transactional
	public Integer add(Exchange t, Merchant m) {
		TenantIdContext.setTenantId(m.getTenant_id());
		///////////////////////////////////////////////////// 录入代付订单/////////////////////////////////////////////////////
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum(StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("DFS" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61); // 盘口发起
		t.setRemark("新增换汇");
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		////////////////////////////////////////////////////// ////////////////////////////////////////////////////////////
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

		///////////////////////////////////////////////////// channelcordernum/////////////////////////////////////////////////////
		String channelcordernum = channelservice.getChannelOrder(t, cl);
		Assert.notNull(channelcordernum, "通道单号获取失败!");
		t.setChannelordernum(channelcordernum);

		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		Merchantaccountorder mao = new Merchantaccountorder();
		mao.setUserid(m.getUserid());
		mao.setMerchantid(m.getId());
		mao.setUsername(m.getName());
		mao.setNkname(m.getNikname());
		mao.setMerchantcode(m.getCode());
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		mao.setExchange(m.getExchange());
		mao.setAmount(t.getMerchantdeal());// 交易费用
		mao.setAmountreceived(t.getMerchantpay());// 总支付费用
		mao.setType(DictionaryResource.ORDERTYPE_22);
		mao.setOrdernum(t.getMerchantordernum());
		mao.setRemark("操作资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费："
				+ m.getOnecost());
		merchantaccountordermapper.post(mao);
		//
		merchantaccountservice.payout(mao);

		///////////////////////////////////////////////////// /////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			Agentaccountorder aat = new Agentaccountorder();
			//
			aat.setAgentid(ag.getId());
			aat.setUserid(ag.getUserid());
			aat.setUsername(ag.getName());
			aat.setNkname(ag.getNkname());
			aat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
			aat.setExchange(ag.getExchange());
			aat.setAmount(t.getMerchantdeal() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getAmount() + ag.getOnecost());// 总费用
			aat.setType(DictionaryResource.ORDERTYPE_20);
			aat.setOrdernum("DFA" + StringUtil.getOrderNum());
			aat.setRemark("操作资金：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getAmount()) + " 手续费："
					+ ag.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			//
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		Channel cll = channelmapper.get(t.getChannelid());
		Channelaccountorder cat = new Channelaccountorder();
		cat.setUserid(cll.getUserid());
		//
		cat.setChannelid(cll.getId());
		cat.setUsername(cll.getName());
		cat.setNkname(cll.getNkname());
		cat.setChannelcode(cll.getCode());
		cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		cat.setAmount(t.getChanneldeal());
		cat.setExchange(cll.getExchange());
		cat.setChannelexchange(cll.getExchange());
		cat.setAmountreceived(t.getChannelpay());
		cat.setType(DictionaryResource.ORDERTYPE_22);
		cat.setOrdernum(t.getChannelordernum());
		cat.setRemark(
				"资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", cat.getAmount()) + " 手续费：" + cll.getOnecost());
		channelaccountordermapper.post(cat);

		//
		channelaccountservice.withdrawamount(cat);

		//
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入

		//
		Integer i = mapper.post(t);

		TenantIdContext.remove();

		return i;
	}

	@Override
	public YtIPage<ExchangeVO> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<ExchangeVO>(Collections.emptyList());
			}
		}
		List<ExchangeVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
		});
		return new YtPageBean<ExchangeVO>(param, list, count);
	}

	@Override
	public void paySuccess(Exchange pt) {
		Exchange t = mapper.get(pt.getId());
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
		channelaccountservice.updateWithdrawamount(cao);
		// 计算渠道数据
		channelservice.updateExchange(t);

		// ------------------更新代付订单-----------------
		t.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		t.setRemark("换汇成功！" + pt.getRemark());
		t.setSuccesstime(DateTimeUtil.getNow());
		t.setBacklong(DateUtil.between(t.getSuccesstime(), t.getCreate_time(), DateUnit.SECOND));

		t.setImgurl(pt.getImgurl());
		//
		mapper.put(t);
	}

	/**
	 * 
	 * 回调失败
	 * 
	 */
	@Override
	@Transactional
	public void payFail(Exchange t) {

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
		t.setRemark("换汇失败！");
		t.setSuccesstime(DateTimeUtil.getNow());
		t.setBacklong(DateTimeUtil.diffDays(t.getSuccesstime(), t.getCreate_time()));
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
		mapper.put(t);
	}
}