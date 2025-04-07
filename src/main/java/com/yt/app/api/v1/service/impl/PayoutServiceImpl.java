package com.yt.app.api.v1.service.impl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.AislechannelMapper;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.api.v1.service.MerchantcustomerbanksService;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.vo.SysSnOrder;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.ChannelBot;
import com.yt.app.common.bot.MerchantBot;
import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:51:11
 */

@Slf4j
@Service
public class PayoutServiceImpl extends YtBaseServiceImpl<Payout, Long> implements PayoutService {
	@Autowired
	private PayoutMapper mapper;
	@Autowired
	private MerchantMapper merchantmapper;
	@Autowired
	private AisleMapper aislemapper;
	@Autowired
	private ChannelMapper channelmapper;
	@Autowired
	private AislechannelMapper aislechannelmapper;
	@Autowired
	private PayoutMerchantaccountorderMapper merchantaccountordermapper;
	@Autowired
	private AgentMapper agentmapper;
	@Autowired
	private AgentaccountorderMapper agentaccountordermapper;
	@Autowired
	private ChannelaccountorderMapper channelaccountordermapper;
	@Autowired
	private PayoutMerchantaccountService merchantaccountservice;
	@Autowired
	private AgentaccountService agentaccountservice;
	@Autowired
	private ChannelaccountService channelaccountservice;
	@Autowired
	private SystemaccountService systemaccountservice;
	@Autowired
	private PayoutMerchantaccountMapper merchantaccountmapper;
	@Autowired
	private MerchantaisleMapper merchantaislemapper;
	@Autowired
	private MerchantcustomerbanksService merchantcustomerbanksservice;
	@Autowired
	private ChannelBot channelbot;
	@Autowired
	private MerchantBot merchantbot;

	@Override
	public Integer submitOrder(Payout t) {
		PayoutMerchantaccount maccount = merchantaccountmapper.getByMerchantId(t.getMerchantid());

		if (t.getAmount() <= 0 || t.getAmount() > maccount.getBalance()) {
			throw new YtException("账户余额不足");
		}
		///////////////////////////////////////////////////// 录入代付订单/////////////////////////////////////////////////////
		Merchant m = merchantmapper.get(t.getMerchantid());

		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}

		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum("out" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("outm" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= t.getAmount() && c.getMin() <= t.getAmount()).collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代付金额超出限额");
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
			Assert.notNull(cl, "没有可用的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelcost(cl.getOnecost());// 渠道手续费
			t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
			t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
			t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		}
		RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);
		// 获取渠道单号
		boolean flage = getChannelOrderNo(t, cl);
		if (flage) {
			channelbot.notifyChannel(cl);
			throw new YtException("渠道错误!");
		}

		///////////////////////////////////////////////////// 计算商户订单
		PayoutMerchantaccountorder mao = new PayoutMerchantaccountorder();
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
		mao.setRemark("代付资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费：" + m.getOnecost());
		merchantaccountordermapper.post(mao);
		merchantaccountservice.withdrawamount(mao);

		Channel cll = channelmapper.get(t.getChannelid());
		Channelaccountorder cat = new Channelaccountorder();
		cat.setUserid(cll.getUserid());
		cat.setChannelid(cll.getId());
		cat.setChannelname(cll.getName());
		cat.setOnecost(cll.getOnecost());
		cat.setNkname(cll.getNkname());
		cat.setChannelcode(cll.getCode());
		cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		cat.setAmount(t.getAmount());
		cat.setDeal(t.getChanneldeal());
		cat.setOnecost(t.getChannelcost());
		cat.setAccname(t.getAccname());
		cat.setAccnumber(t.getAccnumer());
		cat.setExchange(cll.getExchange());
		cat.setChannelexchange(cll.getExchange());
		cat.setAmountreceived(t.getChannelpay());
		cat.setType(DictionaryResource.ORDERTYPE_23);
		cat.setOrdernum(t.getChannelordernum());
		cat.setRemark("代付资金" + cat.getAmount() + " 交易费" + String.format("%.2f", cat.getDeal()) + " 手续费" + cat.getOnecost());
		channelaccountordermapper.post(cat);
		channelaccountservice.withdrawamount(cat);

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
			aat.setType(DictionaryResource.ORDERTYPE_23.toString());
			aat.setOrdernum("PA" + StringUtil.getOrderNum());
			aat.setRemark("代付资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费：" + aat.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance() - t.getChannelpay());
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Payout get(Long id) {
		Payout t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayResultVO query(String merchantordernum) {
		Payout pt = mapper.getByMerchantOrdernum(merchantordernum);
		if (pt == null) {
			throw new YtException("订单不存在!");
		}
		PayResultVO srv = new PayResultVO();
		srv.setBankcode(pt.getBankcode());
		srv.setCode(pt.getStatus());
		srv.setMerchantid(pt.getMerchantcode());
		srv.setMerchantorderid(merchantordernum);
		srv.setPayamt(pt.getAmount());
		return srv;
	}

	// 盘口提交订单
	@Override
	public PayResultVO submit(PaySubmitDTO ss) {

		if (ss.getMerchantid().length() > 10) {
			throw new YtException("商户号错误!");
		}

		Merchant mc = merchantmapper.getByCode(ss.getMerchantid());

		if (mc == null) {
			throw new YtException("商户不存在!");
		}

		if (!mc.getStatus()) {
			throw new YtException("商户被冻结!");
		}

		PayoutMerchantaccount ma = merchantaccountmapper.getByUserId(mc.getUserid());
		if (ma.getBalance() < ss.getPayamt() || ss.getPayamt() <= 0) {
			throw new YtException("账户余额不足");
		}

		Boolean val = PayUtil.Md5Submit(ss, mc.getAppkey());
		if (!val) {
			throw new YtException("签名不正确!");
		}

		Payout pt = mapper.getByMerchantOrdernum(ss.getMerchantorderid());
		if (pt != null) {
			throw new YtException("已经存在的订单!");
		}

		List<Merchantaisle> listmc = merchantaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
			throw new YtException("商戶沒有配置通道!");
		}
		// 下單
		pt = new Payout();
		pt.setAccname(ss.getBankowner());
		pt.setAccnumer(ss.getBanknum());
		pt.setBankcode(ss.getBankcode());
		pt.setBankaddress(ss.getBankaddress());
		pt.setAmount(ss.getPayamt());
		pt.setNotifyurl(ss.getNotifyurl());
		pt.setAisleid(listmc.get(0).getAisleid());
		pt.setBankname(ss.getBankname());
		pt.setMerchantorderid(ss.getMerchantorderid());
		addPayout(pt, mc);
		// 返回
		PayResultVO sr = new PayResultVO();
		sr.setBankcode(ss.getBankcode());
		sr.setMerchantid(sr.getMerchantid());
		sr.setMerchantorderid(ss.getMerchantorderid());
		sr.setPayamt(ss.getPayamt());
		sr.setPayorderid(pt.getMerchantordernum());
		sr.setRemark(ss.getRemark());
		sr.setPaytype(ss.getPaytype());
		sr.setSign(PayUtil.Md5Notify(sr, mc.getAppkey()));
		// 返回给盘口订单号
		return sr;
	}

	public void addPayout(Payout t, Merchant m) {
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
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= t.getAmount() && c.getMin() <= t.getAmount()).collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代付金额超出限额");
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
			Assert.notNull(cl, "没有可用的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelcost(cl.getOnecost());// 渠道手续费
			t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
			t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
			t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		}

		boolean flage = getChannelOrderNo(t, cl);

		if (flage) {
			channelbot.notifyChannel(cl);
			throw new YtException("渠道错误!");
		}

		///////////////////////////////////////////////////// 计算商户订单/////////////////////////////////////////////////////
		PayoutMerchantaccountorder mao = new PayoutMerchantaccountorder();
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
		mao.setRemark("代付资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费：" + m.getOnecost());
		merchantaccountordermapper.post(mao);
		merchantaccountservice.withdrawamount(mao);

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
			aat.setType(DictionaryResource.ORDERTYPE_23.toString());
			aat.setOrdernum("outa" + StringUtil.getOrderNum());
			aat.setRemark("代付资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费：" + aat.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance() - t.getChannelpay());
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		//
		mapper.post(t);
		TenantIdContext.remove();
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
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
	public void paySuccess(Payout pt) {
		Payout t = mapper.get(pt.getId());
		if (t.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			// 计算商户订单/////////////////////////////////////////////////////
			PayoutMerchantaccountorder mao = merchantaccountordermapper.getByOrdernum(t.getMerchantordernum());
			mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
			mao.setImgurl(pt.getImgurl());
			// 商户订单
			merchantaccountordermapper.put(mao);
			// 商户账户
			merchantaccountservice.updateWithdrawamount(mao);
			// 系统账户
			systemaccountservice.updatePayout(t);

			// 计算代理
			if (t.getAgentid() != null) {
				Agentaccountorder aao = agentaccountordermapper.getByOrdernum(t.getAgentordernum());
				aao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
				// 代理订单
				agentaccountordermapper.put(aao);
				// 代理账户
				agentaccountservice.updateTotalincome(aao);
			}

			// 计算渠道
			Channelaccountorder cao = channelaccountordermapper.getByOrdernum(t.getChannelordernum());
			cao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
			// 渠道订单
			channelaccountordermapper.put(cao);
			// 渠道账户
			channelaccountservice.updateWithdrawamount(cao);

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
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
				// 通知
				channelbot.getOrderResultImg(pt.getChannelid(), pt.getOrdernum());
				if (pt.getImgurl() != null && !pt.getImgurl().equals(""))
					merchantbot.sendOrderResultImg(pt.getMerchantid(), pt.getImgurl());
				StringBuffer message = new StringBuffer();
				message.append("\r\n姓名：*" + pt.getAccname() + "*\r\n卡号：" + pt.getAccnumer() + " \r\n金额：" + pt.getAmount() + " \r\n单笔：" + pt.getMerchantcost() + " \r\n状态：成功✔✔✔" + "\r\n*" + DateTimeUtil.getDateTime() + "*");
				merchantbot.sendOrderResultSuccess(pt.getMerchantid(), message.toString());
			}

		}
	}

	/**
	 * 
	 * 手动回调失败
	 * 
	 */
	public void payFail(Payout pt) {
		Payout t = mapper.get(pt.getId());
		if (t.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			// 计算商户订单/////////////////////////////////////////////////////
			PayoutMerchantaccountorder mao = merchantaccountordermapper.getByOrdernum(t.getMerchantordernum());
			mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
			merchantaccountordermapper.put(mao);
			//
			merchantaccountservice.turndownWithdrawamount(mao);

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
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
				StringBuffer message = new StringBuffer();
				message.append("\r\n姓名：*" + pt.getAccname() + "*\r\n卡号：" + pt.getAccnumer() + " \r\n金额：" + pt.getAmount() + " \r\n状态：失败❌❌❌" + "\r\n*" + DateTimeUtil.getDateTime() + "*");
				merchantbot.sendOrderResultFail(pt.getMerchantid(), message.toString());
			}
		}
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayResultVO queryblance(String merchantid) {
		Merchant mt = merchantmapper.getByCode(merchantid);
		Assert.notNull(mt, "没有找到商户!");
		PayoutMerchantaccount mtt = merchantaccountmapper.getByUserId(mt.getUserid());
		PayResultVO srv = new PayResultVO();
		srv.setBalance(mtt.getBalance());
		srv.setMerchantid(merchantid);
		return srv;
	}

	@Override
	public YtBody txcallbackpay(SysTyOrder so) {
		Payout pt = mapper.getByOrdernum(so.getMerchant_order_id());
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			if (pt.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
				Channel cl = channelmapper.get(pt.getChannelid());
				String ip = AuthContext.getIp();
				if (cl.getIpaddress() == null || cl.getIpaddress().indexOf(ip) == -1) {
					throw new YtException("非法请求!");
				}
				// 查询渠道是否真实成功
				SysTyOrder sto = PayUtil.SendTxSelectOrder(pt.getOrdernum(), cl);
				if (sto != null && sto.getPay_message() == 1) {
					// md5值是否被篡改
					if (PayUtil.valMd5TyResultOrder(so, cl.getApikey()) && so.getPay_message() == 1) {
						pt.setImgurl(so.getImage_address());
						paySuccess(pt);
					}
				} else if (sto != null && (sto.getPay_message() == -2 || sto.getPay_message() == -3) && so.getPay_message() == -2) {
					payFail(pt);
				}
			}
		}
		SysUserContext.remove();
		TenantIdContext.remove();
		return new YtBody("成功", 200);
	}

	@Override
	public YtBody exist(SysTyOrder so) {
		if (RedisUtil.hasKey(SystemConstant.CACHE_SYS_PAYOUT_EXIST + so.getMerchant_order_id())) {
			return new YtBody("成功", 200);
		}
		return new YtBody("成功", 400);
	}

	@Override
	public String upFile(MultipartFile file, String aisleid) throws IOException {
		Aisle aisle = aislemapper.get(Long.valueOf(aisleid));
		PayoutMerchantaccount maccount = merchantaccountmapper.getByUserId(SysUserContext.getUserId());
		Merchant m = merchantmapper.getByUserId(SysUserContext.getUserId());
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		Double countamount = 0.0;
		int maxRow = sheet.getLastRowNum();
		for (int i = 1; i <= maxRow; i++) {
			Row row = sheet.getRow(i);
			if (row.getCell(0) != null) {
				countamount = countamount + Double.valueOf(row.getCell(3).toString());
			}
		}
		if (countamount <= 0 || countamount > maccount.getBalance()) {
			throw new YtException("账户余额不足");
		}

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(aisle.getId());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
		double count = 0;
		for (Channel cml : listc) {
			count = count + cml.getWeight();
		}
		for (Channel cmm : listc) {
			weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
		}
		WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
		String code = wr.next();
		Channel cl = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
		Assert.notNull(cl, "没有可用的渠道!");

		for (int i = 1; i <= maxRow; i++) {
			Row row = sheet.getRow(i);
			if (row.getCell(0) != null) {
				importOrder(aisle, row.getCell(0).toString(), row.getCell(1).toString(), row.getCell(2).toString(), Double.valueOf(row.getCell(3).toString()), maccount, m, cl);
			}
		}
		return file.getOriginalFilename();
	}

	public Integer importOrder(Aisle al, String name, String cardno, String bankname, Double amount, PayoutMerchantaccount maccount, Merchant m, Channel cl) {

		Payout t = new Payout();
		t.setAccname(name.replaceAll(" ", ""));
		t.setAccnumer(cardno.replaceAll(" ", ""));
		t.setBankname(bankname.replaceAll(" ", ""));
		t.setAmount(amount);
		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}

		t.setChannelid(cl.getId());
		t.setChannelname(cl.getName());
		t.setChannelcost(cl.getOnecost());// 渠道手续费
		t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
		t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);

		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum("out" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum("outm" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));

		t.setAislename(al.getName());

		RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);

		boolean flage = getChannelOrderNo(t, cl);
		if (flage) {
			channelbot.notifyChannel(cl);
			throw new YtException("渠道错误!");
		}

		///////////////////////////////////////////////////// 计算商户订单
		PayoutMerchantaccountorder mao = new PayoutMerchantaccountorder();
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
		mao.setRemark("代付资金：" + t.getAmount() + " 交易费：" + String.format("%.2f", t.getMerchantdeal()) + " 手续费：" + m.getOnecost());
		merchantaccountordermapper.post(mao);
		merchantaccountservice.withdrawamount(mao);

		Channel cll = channelmapper.get(t.getChannelid());
		Channelaccountorder cat = new Channelaccountorder();
		cat.setUserid(cll.getUserid());
		cat.setChannelid(cll.getId());
		cat.setChannelname(cll.getName());
		cat.setOnecost(cll.getOnecost());
		cat.setNkname(cll.getNkname());
		cat.setChannelcode(cll.getCode());
		cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		cat.setAmount(t.getAmount());
		cat.setDeal(t.getChanneldeal());
		cat.setOnecost(t.getChannelcost());
		cat.setAccname(t.getAccname());
		cat.setAccnumber(t.getAccnumer());
		cat.setExchange(cll.getExchange());
		cat.setChannelexchange(cll.getExchange());
		cat.setAmountreceived(t.getChannelpay());
		cat.setType(DictionaryResource.ORDERTYPE_23);
		cat.setOrdernum(t.getChannelordernum());
		cat.setRemark("代付资金" + cat.getAmount() + " 交易费" + String.format("%.2f", cat.getDeal()) + " 手续费" + cat.getOnecost());
		channelaccountordermapper.post(cat);
		channelaccountservice.withdrawamount(cat);

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
			aat.setType(DictionaryResource.ORDERTYPE_23.toString());
			aat.setOrdernum("outa" + StringUtil.getOrderNum());
			aat.setRemark("代付资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费：" + aat.getOnecost());
			t.setAgentincome(aat.getAmountreceived());
			t.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance() - t.getChannelpay());
		cl.setBalance(cl.getBalance() - t.getChannelpay());
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	boolean getChannelOrderNo(Payout t, Channel cl) {
		// 获取渠道单号
		boolean flage = true;
		switch (cl.getName()) {
		case DictionaryResource.DFSXAISLE:
			String sxordernum = PayUtil.SendSXSubmit(t, cl);
			if (sxordernum != null) {
				flage = false;
				t.setChannelordernum(sxordernum);
			}
			break;
		case DictionaryResource.DFXRAISLE:
			String xrordernum = PayUtil.SendXRSubmit(t, cl);
			if (xrordernum != null) {
				flage = false;
				t.setChannelordernum(xrordernum);
			}
			break;
		case DictionaryResource.DFYSAISLE:
			String ysordernum = PayUtil.SendYSSubmit(t, cl);
			if (ysordernum != null) {
				flage = false;
				t.setChannelordernum(ysordernum);
			}
			break;
		case DictionaryResource.DFSNAISLE:
			String ordernum = PayUtil.SendSnSubmit(t, cl);
			if (ordernum != null) {
				flage = false;
				t.setChannelordernum(ordernum);
			}
			break;
		case DictionaryResource.DFSSAISLE:
			String ssordernum = PayUtil.SendSSSubmit(t, cl);
			if (ssordernum != null) {
				flage = false;
				t.setChannelordernum(ssordernum);
			}
			break;
		case DictionaryResource.DFTXAISLE:
			String txordernum = PayUtil.SendTxSubmit(t, cl);
			if (txordernum != null) {
				flage = false;
				t.setChannelordernum(txordernum);
			}
			break;
		}
		return flage;
	}

	@Override
	public void sncallback(Map<String, Object> params) {
		String orderid = params.get("OrderNo").toString();
		String status = params.get("Status").toString();
		log.info("十年通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			SysSnOrder data = PayUtil.SendSnSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(data, "十年代付通知反查订单失败!");
			if (data.getData().getStatus() == 4) {
				pt.setImgurl(data.getData().getAttachments());
				paySuccess(pt);
			} else if (data.getData().getStatus() == 16) {
				payFail(pt);
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void sscallback(Map<String, Object> params) {
		String orderid = params.get("OrderNo").toString();
		String status = params.get("Status").toString();
		log.info("盛世通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendSSSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "盛世代付通知反查订单失败!");
			if (returnstate == 4) {
				paySuccess(pt);
			} else if (returnstate == 16) {
				payFail(pt);
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void ysdfcallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("易生通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendYSSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "易生代付通知反查订单失败!");
			if (returnstate == 2) {
				paySuccess(pt);
			} else if (returnstate == 3) {
				payFail(pt);
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public Integer success(Long id) {
		Payout pt = mapper.get(id);
		paySuccess(pt);
		return 1;
	}

	@Override
	public Integer fail(Long id) {
		Payout pt = mapper.get(id);
		payFail(pt);
		return 1;
	}

	@Override
	public void xrcallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("旭日通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			String returnstate = PayUtil.SendXRSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "旭日代付通知反查订单失败!");
			if (returnstate.equals("2")) {
				paySuccess(pt);
			} else if (returnstate.equals("3")) {
				payFail(pt);
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}

	}

	@Override
	public void sxcallback(Map<String, String> params) {
		String orderid = params.get("out_trade_no").toString();
		String status = params.get("trade_status").toString();
		log.info("守信通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendSXSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "守信代付通知反查订单失败!");
			if (returnstate == 1) {
				paySuccess(pt);
			} else if (returnstate == 2) {
				payFail(pt);
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}

	}
}