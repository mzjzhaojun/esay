package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.AislechannelMapper;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.mapper.MerchantqrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.model.result.AlipayF2FPrecreateResult;
import com.yt.app.api.v1.model.result.AlipayF2FQueryResult;
import com.yt.app.api.v1.service.AgentService;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.AlipayTradeService;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysFcOrder;
import com.yt.app.api.v1.vo.SysGzOrder;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.api.v1.vo.SysRblOrder;
import com.yt.app.api.v1.vo.SysWdOrder;
import com.yt.app.api.v1.vo.SysWjOrder;
import com.yt.app.api.v1.vo.SysYjjOrder;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.AliPayUtil;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 23:02:54
 */
@Slf4j
@Service
public class IncomeServiceImpl extends YtBaseServiceImpl<Income, Long> implements IncomeService {

	@Autowired
	private YtConfig appConfig;
	@Autowired
	private IncomeMapper mapper;
	@Autowired
	private QrcodeMapper qrcodemapper;
	@Autowired
	private AisleMapper aislemapper;
	@Autowired
	private AgentMapper agentmapper;
	@Autowired
	private MerchantaisleMapper merchantaislemapper;
	@Autowired
	private AislechannelMapper aislechannelmapper;
	@Autowired
	private QrcodeaisleMapper qrcodeaislemapper;
	@Autowired
	private QrcodeaisleqrcodeMapper qrcodeaisleqrcodemapper;
	@Autowired
	private MerchantMapper merchantmapper;
	@Autowired
	private MerchantqrcodeaisleMapper merchantqrcodeaislemapper;
	@Autowired
	private QrcodeaccountorderMapper qrcodeaccountordermapper;
	@Autowired
	private AgentaccountorderMapper agentaccountordermapper;
	@Autowired
	private IncomemerchantaccountorderMapper incomemerchantaccountordermapper;
	@Autowired
	private ChannelMapper channelmapper;
	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;
	@Autowired
	private QrcodeaccountService qrcodeaccountservice;
	@Autowired
	private MerchantService merchantservice;
	@Autowired
	private ChannelService channelservice;
	@Autowired
	private SystemaccountService systemaccountservice;
	@Autowired
	private AgentaccountService agentaccountservice;
	@Autowired
	private AgentService agentservice;
	@Autowired
	private AlipayTradeService tradeService;

	@Override
	@Transactional
	public Integer post(Income t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Income> list(Map<String, Object> param) {
		List<Income> list = mapper.list(param);
		return new YtPageBean<Income>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Income get(Long id) {
		Income t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<IncomeVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomeVO>(Collections.emptyList());
		}
		List<IncomeVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setNotifystatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getNotifystatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<IncomeVO>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Income getByOrderNum(String ordernum) {
		return mapper.getByOrderNum(ordernum);
	}

	@Override
	@Transactional
	public void hscallback(@RequestParam Map<String, String> params) {
		String orderid = params.get("orderid").toString();
		String status = params.get("status").toString();
		log.info("宏盛通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendHSQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "宏盛通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}

	}

	@Override
	@Transactional
	public void yjjcallback(Map<String, String> params) {
		String orderid = params.get("order_id").toString();
		String status = params.get("status").toString();
		log.info("YJJ通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendYJJQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "YJJ通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}

	}

	@Override
	@Transactional
	public void wdcallback(Map<String, String> params) {
		String orderid = params.get("payOrderId").toString();
		String status = params.get("state").toString();
		log.info("豌豆通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByQrcodeOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendWdQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "豌豆通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}
	}

	@Override
	@Transactional
	public void rblcallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("state").toString();
		log.info("日不落通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendRblQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "日不落通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}
	}

	@Override
	@Transactional
	public void fccallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("state").toString();
		log.info("翡翠通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendFcQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "翡翠通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}
	}

	@Override
	@Transactional
	public void gzcallback(Map<String, Object> params) {
		String orderid = params.get("out_trade_no").toString();
		String status = params.get("pay_status").toString();
		log.info("公子通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendGzQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "公子通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}
	}

	@Override
	@Transactional
	public void wjcallback(Map<String, String> params) {
		String orderid = params.get("payOrderId").toString();
		String status = params.get("state").toString();
		log.info("玩家通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByQrcodeOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String returnstate = PayUtil.SendWjQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "玩家通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
			TenantIdContext.remove();
		}
	}

	/**
	 * 自营渠道下单
	 */
	@Override
	@Transactional
	public QrcodeResultVO submitQrcode(QrcodeSubmitDTO qs) {
		// 验证
		Merchant mc = checkparam(qs);

		TenantIdContext.setTenantId(mc.getTenant_id());

		List<Merchantqrcodeaisle> listmc = merchantqrcodeaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
			throw new YtException("商戶还沒有配置通道!");
		}
		long[] qraids = listmc.stream().mapToLong(qa -> qa.getQrcodeaisleid()).distinct().toArray();
		List<Qrcodeaisle> listqa = qrcodeaislemapper.listByArrayId(qraids);
		Qrcodeaisle qas;
		try {
			qas = listqa.stream().filter(qa -> qa.getCode().equals(qs.getPay_aislecode())).findFirst().get();
		} catch (Exception e1) {
			throw new YtException(qs.getPay_aislecode() + "通道没有权限");
		}
		Merchantqrcodeaisle mqd = listmc.stream().filter(mqds -> mqds.getQrcodeaisleid().equals(qas.getId())).findFirst().get();
		Assert.notNull(mqd, "通道没有权限!");
		List<Qrcodeaisleqrcode> listqaq = qrcodeaisleqrcodemapper.getByQrcodeAisleId(qas.getId());
		long[] qaqids = listqaq.stream().mapToLong(qaq -> qaq.getQrcodelid()).distinct().toArray();
		List<Qrcode> listqrcode = qrcodemapper.listByArrayId(qaqids);
		Double amount = Double.valueOf(qs.getPay_amount());
		List<Qrcode> listcmm = listqrcode.stream().filter(c -> c.getMax() >= amount && c.getMin() <= amount).collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代收金额超出限额");
		List<Qrcode> listcf = listcmm.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Qrcode qd = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Qrcode qc = listcf.get(j);
				String[] match = qc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && amount == Integer.parseInt(number)) {
						qd = qc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (amount >= min && amount <= max) {
							qd = qc;
						}
					}
				}
			}
		} else {
			// 根据权重选择码
			List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
			double count = 0;
			for (Qrcode cml : listcmm) {
				count = count + cml.getWeight();
			}
			for (Qrcode cmm : listcmm) {
				weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
			}
			WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
			String code = wr.next();
			qd = listqrcode.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
			Assert.notNull(qd, "没有可用的渠道!");
		}
		// 开始业务
		Channel channel = channelmapper.getByUserId(qd.getUserid());
		Income income = addIncome(channel, qas, mc, qs, qd);
		// 动态码直接去线上拿连接
		if (qd.getDynamic()) {
			// 阿里当面付和订单码
			AlipayTradePrecreateResponse atp = alif2f(qd, income.getOrdernum(), income.getAmount(), qd.getExpireminute());
			Assert.notNull(atp, "获取支付宝单号错误!");
			income.setQrcode(atp.getQrCode());
			income.setQrcodeordernum("in_come_qrcode" + StringUtil.getOrderNum());
			income.setResulturl(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
		} else {
			income.setResulturl(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
		}
		Assert.notNull(mqd.getCollection(), "渠道点位未配置!");
		///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
		if (mc.getAgentid() != null) {
			Agent ag = agentmapper.get(mc.getAgentid());
			income.setAgentid(ag.getId());
			Agentaccountorder aat = new Agentaccountorder();
			aat.setAgentid(ag.getId());
			aat.setUserid(ag.getUserid());
			aat.setUsername(ag.getName());
			aat.setNkname(ag.getNkname());
			aat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
			aat.setExchange(ag.getExchange());
			aat.setAmount(income.getAmount());// 金额
			aat.setDeal(income.getAmount() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
			aat.setOnecost(ag.getOnecost());// 手续费
			aat.setType(income.getType());
			aat.setOrdernum("income_agent_" + StringUtil.getOrderNum());
			aat.setRemark("代收资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费：" + aat.getOnecost());
			income.setAgentincome(aat.getAmountreceived());
			income.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			income.setAgentincome(0.00);
		}
		// 渠道收入
		income.setChannelincomeamount(NumberUtil.multiply(income.getAmount().toString(), (channel.getCollection() / 100) + "", 2).doubleValue());
		income.setMerchantincomeamount(NumberUtil.multiply(income.getAmount().toString(), (mqd.getCollection() / 100) + "", 2).doubleValue());
		// 系统收入
		income.setIncomeamount(Double.valueOf(String.format("%.2f", (income.getMerchantincomeamount() - income.getChannelincomeamount()))));
		// 商户收入
		income.setMerchantincomeamount(Double.valueOf(String.format("%.2f", (income.getAmount() - income.getMerchantincomeamount() - income.getAgentincome()))));
		// 计算当前码可生成的订单
		RLock lock = RedissonUtil.getLock(qd.getId());
		Integer i = 0;
		income.setType(qd.getType());
		try {
			lock.lock();
			if (qd.getDynamic()) {
				income.setFewamount(0.00);
				income.setRealamount(income.getAmount());
				i = mapper.post(income);
			} else {
				Double fewamount = NumberUtil.getIncomeFewAmount(qd.getId());
				if (fewamount < 3) {
					income.setFewamount(fewamount);
					income.setRealamount(income.getAmount() - fewamount);
					i = mapper.post(income);
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		if (i == 0) {
			throw new YtException("当前通道码繁忙");
		}
		QrcodeResultVO result = addOtherOrderMqd(income, channel, qas, mc, qs);
		TenantIdContext.remove();
		return result;
	}

	/**
	 * 上游渠道下单
	 */
	@Override
	@Transactional
	public QrcodeResultVO submitInCome(QrcodeSubmitDTO qs) {
		// 验证
		Merchant mc = checkparam(qs);
		TenantIdContext.setTenantId(mc.getTenant_id());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		Aisle aisle = aislemapper.getByCode(qs.getPay_aislecode());
		Assert.notNull(aisle, "没有可用通道!");
		Merchantaisle opt = merchantaislemapper.getByMidAid(aisle.getId(), mc.getId());
		Assert.notNull(opt, "没有可用通道!");
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(aisle.getId());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= Double.valueOf(qs.getPay_amount()) && c.getMin() <= Double.valueOf(qs.getPay_amount())).collect(Collectors.toList());
		Assert.notEmpty(listcmm, "金额超出限额!");
		List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Channel channel = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Channel cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && Double.valueOf(qs.getPay_amount()) == Double.valueOf(number)) {
						channel = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (Double.valueOf(qs.getPay_amount()) >= min && Double.valueOf(qs.getPay_amount()) <= max) {
							channel = cc;
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
			channel = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
			Assert.notNull(channel, "没有可用的渠道!");
		}

		Income income = new Income();
		// 商戶
		income.setMerchantuserid(mc.getUserid());
		income.setOrdernum("in_come" + StringUtil.getOrderNum());
		income.setMerchantorderid(qs.getPay_orderid());
		income.setMerchantordernum("in_come_mct" + StringUtil.getOrderNum());
		income.setMerchantcode(mc.getCode());
		income.setMerchantname(mc.getName());
		income.setMerchantid(mc.getId());
		income.setExpireddate(DateTimeUtil.addMinute(10));// 默认10分钟
		// 通道
		income.setExpiredminute(10);
		income.setDynamic(aisle.getDynamic());
		income.setQrcodeaisleid(aisle.getId());
		income.setQrcodeaislename(aisle.getName());
		income.setQrcodeaislecode(aisle.getCode());
		// 渠道产品
		income.setQrcodeid(channel.getId());
		income.setQrcodecode(channel.getAislecode());
		income.setQrcodename(channel.getName());
		income.setQrcodeuserid(channel.getUserid());
		// 收款码
		income.setAmount(Double.valueOf(qs.getPay_amount()));

		income.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
		income.setNotifyurl(qs.getPay_notifyurl());
		income.setBackforwardurl(qs.getPay_callbackurl());
		// 远程当面付
		switch (channel.getName()) {
		case DictionaryResource.YJJAISLE:
			SysYjjOrder syo = PayUtil.SendYJJSubmit(income, channel);
			Assert.notNull(syo, "雨将军获取渠道订单失败!");
			income.setResulturl(syo.getData().getPay_url());
			income.setQrcodeordernum(syo.getData().getOrder_id());
			break;
		case DictionaryResource.HSAISLE:
			SysHsOrder sho = PayUtil.SendHSSubmit(income, channel);
			Assert.notNull(sho, "宏盛获取渠道订单失败!");
			income.setResulturl(sho.getPay_url());
			income.setQrcodeordernum(sho.getSys_order_no());
			break;
		case DictionaryResource.WDAISLE:
			SysWdOrder wd = PayUtil.SendWdSubmit(income, channel);
			Assert.notNull(wd, "豌豆获取渠道订单失败!");
			income.setResulturl(wd.getData().getPayData());
			income.setQrcodeordernum(wd.getData().getPayOrderId());
			break;
		case DictionaryResource.RBLAISLE:
			SysRblOrder rbl = PayUtil.SendRblSubmit(income, channel);
			Assert.notNull(rbl, "日不落获取渠道订单失败!");
			income.setResulturl(rbl.getData().getPayUrl());
			income.setQrcodeordernum(rbl.getData().getTradeNo());
			break;
		case DictionaryResource.GZAISLE:
			SysGzOrder gz = PayUtil.SendGzSubmit(income, channel);
			Assert.notNull(gz, "公子获取渠道订单失败!");
			income.setResulturl(gz.getResponse().getPay_url());
			income.setQrcodeordernum(gz.getResponse().getOrder_id());
			break;
		case DictionaryResource.WJAISLE:
			SysWjOrder wj = PayUtil.SendWjSubmit(income, channel);
			Assert.notNull(wj, "玩家获取渠道订单失败!");
			income.setResulturl(wj.getData().getPayData());
			income.setQrcodeordernum(wj.getData().getPayOrderId());
			break;
		case DictionaryResource.FCAISLE:
			SysFcOrder fc = PayUtil.SendFcSubmit(income, channel);
			Assert.notNull(fc, "翡翠获取渠道订单失败!");
			income.setResulturl(fc.getData().getPayUrl());
			income.setQrcodeordernum(fc.getData().getTradeNo());
			break;
		}
		if (mc.getAgentid() != null) {
			Agent ag = agentmapper.get(mc.getAgentid());
			income.setAgentid(ag.getId());
			Agentaccountorder aat = new Agentaccountorder();
			aat.setAgentid(ag.getId());
			aat.setUserid(ag.getUserid());
			aat.setUsername(ag.getName());
			aat.setNkname(ag.getNkname());
			aat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
			aat.setExchange(ag.getExchange());
			aat.setAmount(income.getAmount());// 金额
			aat.setDeal(income.getAmount() * (ag.getExchange() / 100));// 交易费
			aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
			aat.setOnecost(ag.getOnecost());// 手续费
			aat.setType(income.getType());
			aat.setOrdernum("income_agent_" + StringUtil.getOrderNum());
			aat.setRemark("代收资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费：" + aat.getOnecost());
			income.setAgentincome(aat.getAmountreceived());
			income.setAgentordernum(aat.getOrdernum());
			agentaccountordermapper.post(aat);
			agentaccountservice.totalincome(aat);
		} else {
			income.setAgentincome(0.00);
		}
		// 渠道收入
		income.setChannelincomeamount(NumberUtil.multiply(income.getAmount().toString(), (channel.getCollection() / 100) + "", 2).doubleValue());
		// 商户收入
		income.setMerchantincomeamount(NumberUtil.multiply(income.getAmount().toString(), (mc.getCollection() / 100) + "", 2).doubleValue());
		// 系统收入
		income.setIncomeamount(Double.valueOf(String.format("%.2f", (income.getMerchantincomeamount() - income.getChannelincomeamount()))));
		// 商户收入
		income.setMerchantincomeamount(Double.valueOf(String.format("%.2f", (income.getAmount() - income.getMerchantincomeamount() - income.getAgentincome()))));
		// 计算当前码可生成的订单
		income.setFewamount(0.00);
		income.setRealamount(income.getAmount());
		income.setType(DictionaryResource.PROJECT_TYPE_512.toString());
		int i = mapper.post(income);
		if (i == 0) {
			throw new YtException("当前通道码繁忙");
		}
		QrcodeResultVO result = addOtherOrder(income, channel, aisle, mc, qs);
		TenantIdContext.remove();
		return result;
	}

	@Override
	@Transactional
	public QueryQrcodeResultVO queryInCome(QrcodeSubmitDTO qs) {
		// 验证
		if (qs.getPay_memberid().length() > 10) {
			throw new YtException("商户号错误!");
		}
		// 盘口商户
		Merchant mc = merchantmapper.getByCode(qs.getPay_memberid());
		if (mc == null) {
			throw new YtException("商户不存在!");
		}
		String ip = AuthContext.getIp();
		if (mc.getIpaddress() == null || mc.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		String sign = PayUtil.SignMd5QueryQrocde(qs, mc.getAppkey());
		if (!sign.equals(qs.getPay_md5sign())) {
			throw new YtException("签名不正确!");
		}
		Income income = mapper.getByMerchantOrderNum(qs.getPay_orderid());
		QueryQrcodeResultVO qrv = new QueryQrcodeResultVO();
		qrv.setPay_code(income.getStatus());
		qrv.setPay_amount(income.getAmount().toString());
		qrv.setPay_memberid(mc.getCode());
		qrv.setPay_orderid(qs.getPay_orderid());
		qrv.setPay_md5sign(PayUtil.SignMd5QueryResultQrocde(qrv, mc.getAppkey()));
		return qrv;
	}

	@Transactional
	public AlipayTradePrecreateResponse alif2f(Qrcode qrcode, String ordernum, Double amount, Integer exp) {
		try {
			AlipayClient client = AliPayUtil.initAliPay(qrcode.getAppid(), qrcode.getAppprivatekey(), qrcode.getApppublickey(), qrcode.getAlipaypublickey(), qrcode.getAlipayprovatekey());
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
			AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
			model.setOutTradeNo(ordernum);
			model.setTotalAmount(amount.toString());
			model.setSubject(qrcode.getName() + StringUtil.getUUID());
			model.setTimeoutExpress(exp + "m");
			request.setBizModel(model);
			request.setNotifyUrl(qrcode.getNotifyurl());
			AlipayF2FPrecreateResult result = tradeService.tradePrecreate(request, client);
			switch (result.getTradeStatus()) {
			case SUCCESS:
				log.info("支付宝预下单成功: )");
				AlipayTradePrecreateResponse response = result.getResponse();
				log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
				log.info(response.getBody());
				return response;
			case FAILED:
				log.error("支付宝预下单失败!!!");
				break;
			case UNKNOWN:
				log.error("系统异常，预下单状态未知!!!");
				break;
			default:
				log.error("不支持的交易状态，交易返回异常!!!");
				break;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public void alipayftfcallback(Map<String, String> params) {
		String trade_no = params.get("trade_no").toString();
		String out_trade_no = params.get("out_trade_no").toString();
		Income income = mapper.getByOrderNum(out_trade_no);
		Qrcode qrcode = qrcodemapper.get(income.getQrcodeid());
		try {
			AlipayClient client = AliPayUtil.initAliPay(qrcode.getAppid(), qrcode.getAppprivatekey(), qrcode.getApppublickey(), qrcode.getAlipaypublickey(), qrcode.getAlipayprovatekey());
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setTradeNo(trade_no);
			request.setBizModel(model);
			AlipayF2FQueryResult result = tradeService.queryTradeResult(request, client);
			switch (result.getTradeStatus()) {
			case SUCCESS:
				log.info("支付宝查单成功: )");
				AlipayTradeQueryResponse response = result.getResponse();
				if (response.getCode().equals(DictionaryResource.SUCCESS)) {
					success(income, trade_no);
				}
				break;
			case FAILED:
				log.error("支付宝预下单失败!!!");
				break;
			case UNKNOWN:
				log.error("系统异常，预下单状态未知!!!");
				break;
			default:
				log.error("不支持的交易状态，交易返回异常!!!");
				break;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	private Merchant checkparam(QrcodeSubmitDTO qs) {
		if (qs.getPay_memberid().length() > 10) {
			throw new YtException("商户号错误!");
		}
		if (Double.valueOf(qs.getPay_amount()) < 1) {
			throw new YtException("支付金额错误!");
		}
		Merchant mc = merchantmapper.getByCode(qs.getPay_memberid());
		Assert.notNull(mc, "商户不存在!");
		String ip = AuthContext.getIp();
		if (mc.getIpaddress() == null || mc.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求!");
		}
		if (!mc.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		String sign = PayUtil.SignMd5SubmitQrocde(qs, mc.getAppkey());
		log.info(sign);
		if (!sign.equals(qs.getPay_md5sign())) {
			throw new YtException("签名不正确!");
		}
		Income income = mapper.getByMerchantOrderNum(qs.getPay_orderid());
		Assert.isNull(income, "已经存在订单!");
		return mc;
	}

	@Transactional
	private Income addIncome(Channel channel, Qrcodeaisle qas, Merchant mc, QrcodeSubmitDTO qs, Qrcode qd) {
		Income income = new Income();
		// 商戶
		income.setMerchantuserid(mc.getUserid());
		income.setOrdernum("in_come" + StringUtil.getOrderNum());
		income.setMerchantorderid(qs.getPay_orderid());
		income.setMerchantordernum("in_come_mct" + StringUtil.getOrderNum());
		income.setMerchantcode(mc.getCode());
		income.setMerchantname(mc.getName());
		income.setMerchantid(mc.getId());
		income.setExpireddate(DateTimeUtil.addMinute(qd.getExpireminute()));// 多加3分钟
		// 通道
		income.setExpiredminute(qd.getExpireminute());
		income.setQrcodeaisleid(qas.getId());
		income.setQrcodeaislename(qas.getName());
		income.setQrcodeaislecode(qas.getCode());
		// 收款产品
		income.setQrcodeuserid(qd.getUserid());
		income.setQrcodeid(qd.getId());
		income.setQrcodename(qd.getName());
		income.setQrcodecode(qd.getCode());

		income.setAmount(Double.valueOf(qs.getPay_amount()));
		income.setDynamic(qas.getDynamic());

		income.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
		income.setNotifyurl(qs.getPay_notifyurl());
		income.setBackforwardurl(qs.getPay_callbackurl());
		return income;
	}

	@Transactional
	private QrcodeResultVO addOtherOrder(Income income, Channel channel, Aisle qas, Merchant mc, QrcodeSubmitDTO qs) {

		// 添加qrcode订单
		Qrcodeaccountorder qao = new Qrcodeaccountorder();
		qao.setUserid(income.getQrcodeuserid());
		qao.setQrcodeaisleid(income.getQrcodeaisleid());
		qao.setQrcodeaislename(income.getQrcodeaislename());
		qao.setQrcodename(income.getQrcodename());
		qao.setQrcodeid(income.getQrcodeid());
		qao.setOrdernum(income.getQrcodeordernum());
		qao.setType(income.getType());
		qao.setFewamount(income.getFewamount());
		qao.setAmount(income.getAmount());
		qao.setRealamount(income.getRealamount());
		qao.setResulturl(income.getResulturl());
		qao.setQrcodecode(income.getQrcodecode());
		qao.setMerchantname(income.getMerchantname());
		qao.setQrocde(income.getQrcode());
		qao.setDynamic(qas.getDynamic());
		qao.setStatus(income.getStatus());
		qao.setQrcodeaislecode(qas.getCode());
		qao.setChannelid(channel.getId());
		qao.setExpireddate(income.getExpireddate());
		qao.setIncomeamount(income.getChannelincomeamount());
		qrcodeaccountordermapper.post(qao);
		qrcodeaccountservice.totalincome(qao);
		// 添加商戶订单
		Incomemerchantaccountorder imao = new Incomemerchantaccountorder();
		imao.setUserid(income.getMerchantuserid());
		imao.setQrcodeaisleid(income.getQrcodeaisleid());
		imao.setQrcodeaislename(income.getQrcodeaislename());
		imao.setQrcodename(income.getQrcodename());
		imao.setQrcodeid(income.getQrcodeid());
		imao.setOrdernum(income.getMerchantorderid());
		imao.setDynamic(qas.getDynamic());
		imao.setType(income.getType());
		imao.setFewamount(income.getFewamount());
		imao.setAmount(income.getAmount());
		imao.setRealamount(income.getRealamount());
		imao.setResulturl(income.getResulturl());
		imao.setQrcodecode(income.getQrcodecode());
		imao.setMerchantname(income.getMerchantname());
		imao.setQrocde(income.getQrcode());
		imao.setStatus(income.getStatus());
		imao.setQrcodeaislecode(qas.getCode());
		imao.setMerchantid(mc.getId());
		imao.setExpireddate(income.getExpireddate());
		imao.setIncomeamount(income.getMerchantincomeamount());
		incomemerchantaccountordermapper.post(imao);
		incomemerchantaccountservice.totalincome(imao);

		QrcodeResultVO qr = new QrcodeResultVO();
		qr.setPay_memberid(mc.getCode());
		qr.setPay_orderid(qs.getPay_orderid());
		qr.setPay_amount(qs.getPay_amount());
		qr.setPay_aislecode(qs.getPay_aislecode());
		qr.setPay_viewurl(income.getResulturl());
		String signresult = PayUtil.SignMd5ResultQrocde(qr, mc.getAppkey());
		qr.setPay_md5sign(signresult);
		return qr;
	}

	@Transactional
	private QrcodeResultVO addOtherOrderMqd(Income income, Channel channel, Qrcodeaisle qas, Merchant mc, QrcodeSubmitDTO qs) {
		// 添加qrcode订单
		Qrcodeaccountorder qao = new Qrcodeaccountorder();
		qao.setUserid(income.getQrcodeuserid());
		qao.setQrcodeaisleid(income.getQrcodeaisleid());
		qao.setQrcodeaislename(income.getQrcodeaislename());
		qao.setQrcodename(income.getQrcodename());
		qao.setQrcodeid(income.getQrcodeid());
		qao.setOrdernum(income.getQrcodeordernum());
		qao.setType(income.getType());
		qao.setFewamount(income.getFewamount());
		qao.setAmount(income.getAmount());
		qao.setRealamount(income.getRealamount());
		qao.setResulturl(income.getResulturl());
		qao.setQrcodecode(income.getQrcodecode());
		qao.setMerchantname(income.getMerchantname());
		qao.setQrocde(income.getQrcode());
		qao.setDynamic(qas.getDynamic());
		qao.setStatus(income.getStatus());
		qao.setQrcodeaislecode(qas.getCode());
		qao.setChannelid(channel.getId());
		qao.setExpireddate(income.getExpireddate());
		qao.setIncomeamount(income.getChannelincomeamount());
		qrcodeaccountordermapper.post(qao);
		qrcodeaccountservice.totalincome(qao);
		// 添加商戶订单
		Incomemerchantaccountorder imao = new Incomemerchantaccountorder();
		imao.setUserid(income.getMerchantuserid());
		imao.setQrcodeaisleid(income.getQrcodeaisleid());
		imao.setQrcodeaislename(income.getQrcodeaislename());
		imao.setQrcodename(income.getQrcodename());
		imao.setQrcodeid(income.getQrcodeid());
		imao.setOrdernum(income.getMerchantorderid());
		imao.setDynamic(qas.getDynamic());
		imao.setType(income.getType());
		imao.setFewamount(income.getFewamount());
		imao.setAmount(income.getAmount());
		imao.setQrcodecode(income.getQrcodecode());
		imao.setRealamount(income.getRealamount());
		imao.setResulturl(income.getResulturl());
		imao.setMerchantname(income.getMerchantname());
		imao.setQrocde(income.getQrcode());
		imao.setStatus(income.getStatus());
		imao.setQrcodeaislecode(qas.getCode());
		imao.setMerchantid(mc.getId());
		imao.setExpireddate(income.getExpireddate());
		imao.setIncomeamount(income.getMerchantincomeamount());
		incomemerchantaccountordermapper.post(imao);
		incomemerchantaccountservice.totalincome(imao);

		QrcodeResultVO qr = new QrcodeResultVO();
		qr.setPay_memberid(mc.getCode());
		qr.setPay_orderid(qs.getPay_orderid());
		qr.setPay_amount(qs.getPay_amount());
		qr.setPay_aislecode(qs.getPay_aislecode());
		qr.setPay_viewurl(income.getResulturl());
		String signresult = PayUtil.SignMd5ResultQrocde(qr, mc.getAppkey());
		qr.setPay_md5sign(signresult);
		return qr;
	}

	@Transactional
	private void success(Income income) {
		// 計算代收
		income.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		if (income.getNotifystatus() == DictionaryResource.PAYOUTNOTIFYSTATUS_61)
			income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
		income.setSuccesstime(DateTimeUtil.getNow());
		income.setBacklong(DateUtil.between(income.getSuccesstime(), income.getCreate_time(), DateUnit.SECOND));
		//
		mapper.put(income);
		// 渠道
		Qrcodeaccountorder qrcodeaccountorder = qrcodeaccountordermapper.getByOrderNum(income.getQrcodeordernum());
		qrcodeaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		qrcodeaccountordermapper.put(qrcodeaccountorder);
		// 计算渠道收入
		qrcodeaccountservice.updateTotalincome(qrcodeaccountorder);
		//
		Incomemerchantaccountorder incomemerchantaccountorder = incomemerchantaccountordermapper.getByOrderNum(income.getMerchantorderid());
		incomemerchantaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		incomemerchantaccountordermapper.put(incomemerchantaccountorder);
		// 计算商户收入
		incomemerchantaccountservice.updateTotalincome(incomemerchantaccountorder);
		// 计算商户主账号
		merchantservice.updateIncome(income);
		// 计算渠道主账号
		channelservice.updateIncome(income);
		// 计算系统收入
		systemaccountservice.updateIncome(income);
	}

	@Transactional
	private void success(Income income, String trade_no) {
		TenantIdContext.setTenantId(income.getTenant_id());
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			// 渠道
			Qrcodeaccountorder qrcodeaccountorder = qrcodeaccountordermapper.getByOrderNum(income.getQrcodeordernum());
			qrcodeaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			qrcodeaccountorder.setOrdernum(trade_no);
			qrcodeaccountordermapper.put(qrcodeaccountorder);
			// 計算代收
			income.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			income.setQrcodeordernum(trade_no);
			if (income.getNotifystatus() == DictionaryResource.PAYOUTNOTIFYSTATUS_61)
				income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			income.setSuccesstime(DateTimeUtil.getNow());
			income.setBacklong(DateUtil.between(income.getSuccesstime(), income.getCreate_time(), DateUnit.SECOND));

			// 计算代理
			if (income.getAgentid() != null) {
				Agentaccountorder aao = agentaccountordermapper.getByOrdernum(income.getAgentordernum());
				aao.setStatus(DictionaryResource.PAYOUTSTATUS_52);
				// 代理订单
				agentaccountordermapper.put(aao);
				// 代理账户
				agentaccountservice.updateTotalincome(aao);
				// 计算代理数据
				agentservice.updateIncome(income);
			}
			//
			mapper.put(income);
			// 计算渠道收入
			qrcodeaccountservice.updateTotalincome(qrcodeaccountorder);
			//
			Incomemerchantaccountorder incomemerchantaccountorder = incomemerchantaccountordermapper.getByOrderNum(income.getMerchantorderid());
			incomemerchantaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			incomemerchantaccountordermapper.put(incomemerchantaccountorder);
			// 计算商户收入
			incomemerchantaccountservice.updateTotalincome(incomemerchantaccountorder);
			// 计算商户主账号
			merchantservice.updateIncome(income);
			// 计算渠道主账号
			channelservice.updateIncome(income);
			// 计算系统收入
			systemaccountservice.updateIncome(income);
			TenantIdContext.remove();
		}

	}

	@Override
	@Transactional
	public Integer makeuporder(Income inc) {
		Income newincome = mapper.get(inc.getId());

		newincome.setId(null);
		newincome.setOrdernum("in_come" + StringUtil.getOrderNum());
		newincome.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		newincome.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
		Integer i = mapper.post(newincome);

		Qrcodeaccountorder newqrcodeaccountorder = qrcodeaccountordermapper.getByOrderNum(newincome.getQrcodeordernum());
		newqrcodeaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		qrcodeaccountordermapper.put(newqrcodeaccountorder);
		qrcodeaccountservice.totalincome(newqrcodeaccountorder);

		Incomemerchantaccountorder newincomemerchantaccountorder = incomemerchantaccountordermapper.getByOrderNum(newincome.getMerchantorderid());
		newincomemerchantaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		incomemerchantaccountordermapper.put(newincomemerchantaccountorder);
		incomemerchantaccountservice.totalincome(newincomemerchantaccountorder);
		//
		success(newincome);
		return i;
	}

	@Override
	@Transactional
	public Integer notify(Income income) {
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
		return mapper.put(income);
	}

	@Override
	@Transactional
	public Integer addip() {
		Merchant mt = merchantmapper.getByCode("SH00001");
		mt.setIpaddress(mt.getIpaddress() + "," + AuthContext.getIp());
		merchantmapper.put(mt);
		return null;
	}
}