package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.AislechannelMapper;
import com.yt.app.api.v1.mapper.BlocklistMapper;
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
import com.yt.app.api.v1.mapper.QrcodpaymemberMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.QrcodeService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.ChannelBot;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeSettleConfirmResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Blocklist;
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
import com.yt.app.api.v1.entity.Qrcodpaymember;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysFcOrder;
import com.yt.app.api.v1.vo.SysFhOrder;
import com.yt.app.api.v1.vo.SysGzOrder;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.api.v1.vo.SysYSOrder;
import com.yt.app.api.v1.vo.SysRblOrder;
import com.yt.app.api.v1.vo.SysWdOrder;
import com.yt.app.api.v1.vo.SysWjOrder;
import com.yt.app.api.v1.vo.SysXSOrder;
import com.yt.app.api.v1.vo.SysTdOrder;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.SelfPayUtil;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.MD5Utils;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	private QrcodeService qrcodeservice;
	@Autowired
	private SystemaccountService systemaccountservice;
	@Autowired
	private AgentaccountService agentaccountservice;
	@Autowired
	private ChannelBot channelbot;
	@Autowired
	private BlocklistMapper blocklistmapper;
	@Autowired
	private QrcodpaymemberMapper qrcodpaymembermapper;

	@Override
	@Transactional
	public Integer post(Income t) {
		Integer i = mapper.post(t);
		return i;
	}

	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Income> list() {
		Map<String, Object> param = new HashMap<String, Object>();
		List<Income> list = mapper.list(param);
		list.forEach(ii -> {
			ii.setQrcodeaislename(MD5Utils.randomName(true, 3));
		});
		return list;
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
	public void kfcallback(@RequestParam Map<String, String> params) {
		String orderid = params.get("orderid").toString();
		String status = params.get("status").toString();
		log.info("kf通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendKFQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "kf通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void egcallback(Map<String, String> params) {
		String orderid = params.get("orderid").toString();
		String status = params.get("returncode").toString();
		log.info("二狗通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendEgQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "二狗通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void wdcallback(Map<String, String> params) {
		String orderid = params.get("payOrderId").toString();
		String status = params.get("state").toString();
		log.info("豌豆通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByQrcodeOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendWdQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "豌豆通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void rblcallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("state").toString();
		log.info("日不落通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendRblQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "日不落通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void fccallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("state").toString();
		log.info("翡翠通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendFcQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "翡翠通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void aklcallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("state").toString();
		log.info("奥克兰通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendAklQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "奥克兰通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void gzcallback(Map<String, String> params) {
		String orderid = params.get("orderId").toString();
		String status = params.get("status").toString();
		log.info("公子通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendGzQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "公子通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void fhcallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("飞黄运通通知返回消息：payOrderId" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendFhQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "飞黄运通通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void tongyuancallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("state").toString();
		log.info("通源通知返回消息：payOrderId" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		JSONObject returnstate = PayUtil.SendTongYuanQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "通源通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void onepluscallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("state").toString();
		log.info("oneplus通知返回消息：payOrderId" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		JSONObject returnstate = PayUtil.SendOnePlusQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "oneplus通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void yscallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("易生通知返回消息：payOrderId" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendYSQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "易生通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void wjcallback(Map<String, String> params) {
		String orderid = params.get("payOrderId").toString();
		String status = params.get("state").toString();
		log.info("玩家通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByQrcodeOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendWjQuerySubmit(orderid, income.getAmount(), channel);
		Assert.notNull(returnstate, "玩家通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void xscallback(Map<String, String> params) {
		String orderid = params.get("orderCode").toString();
		String status = params.get("status").toString();
		log.info("新生通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByQrcodeOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		String returnstate = PayUtil.SendXSQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "新生通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void zscallback(Map<String, Object> params) {
		String orderid = params.get("OrderNo").toString();
		String status = params.get("Status").toString();
		log.info("张三通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getQrcodeid());
		String ip = AuthContext.getIp();
		if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
			throw new YtException("非法请求,IP加白名单后重试!");
		}
		JSONObject returnstate = PayUtil.SendZSQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "张三通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income);
		}
		TenantIdContext.remove();
	}

	@Override
	public void alipayftfcallback(Map<String, String> params) {
		String trade_no = params.get("trade_no").toString();
		String out_trade_no = params.get("out_trade_no").toString();
		log.info("支付宝通知返回消息：trade_no" + trade_no + " out_trade_no:" + out_trade_no);
		Income income = mapper.getByOrderNum(out_trade_no);
		TenantIdContext.setTenantId(income.getTenant_id());
		Qrcode qrcode = qrcodemapper.get(income.getQrcodeid());
		Qrcode pqrcode = qrcodemapper.get(qrcode.getPid());
		log.info("支付宝查单成功: " + trade_no + "===" + out_trade_no);
		if (income.getStatus() == DictionaryResource.PAYOUTSTATUS_50) {
			AlipayTradeQueryResponse atqr = SelfPayUtil.AlipayTradeWapQuery(pqrcode, out_trade_no, trade_no);
			if (atqr.getTradeStatus().equals("TRADE_SUCCESS")) {
				success(income, trade_no);
			}
		}
		TenantIdContext.remove();
	}

	/**
	 * 上游渠道下单
	 */
	@Override
	public QrcodeResultVO submitInCome(QrcodeSubmitDTO qs) {
		// 验证
		Merchant mc = checkparam(qs);
		TenantIdContext.setTenantId(mc.getTenant_id());
		//
		boolean flage = true;
		List<Merchantqrcodeaisle> listmc = merchantqrcodeaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
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
			List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= Double.valueOf(qs.getPay_amount()) && c.getMin() <= Double.valueOf(qs.getPay_amount()) && c.getStatus()).collect(Collectors.toList());
			Assert.notEmpty(listcmm, "金额超出限额!");
			// 有限匹配
			List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
			Channel channel = null;
			if (listcf.size() > 0) {
				for (int j = 0; j < listcf.size(); j++) {
					Channel cc = listcf.get(j);
					String[] match = cc.getFirstmatchmoney().split(",");
					for (int i = 0; i < match.length; i++) {
						String number = match[i];
						if (number.indexOf("-") == -1 && (Integer.valueOf(qs.getPay_amount()) == Integer.valueOf(number))) {
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
				// 随机权重
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
			income.setOrdernum("in" + StringUtil.getOrderNum());
			income.setMerchantorderid(qs.getPay_orderid());
			income.setMerchantordernum("inm" + qs.getPay_orderid());
			income.setMerchantcode(mc.getCode());
			income.setMerchantname(mc.getName());
			income.setMerchantid(mc.getId());
			income.setExpireddate(DateTimeUtil.addMinute(channel.getMtorder()));// 默认30分钟
			// 通道
			income.setExpiredminute(channel.getMtorder());
			income.setQrcodeaisleid(aisle.getId());
			income.setQrcodeaislename(aisle.getName());
			income.setQrcodeaislecode(aisle.getCode());
			// 渠道产品
			income.setQrcodeid(channel.getId());
			income.setQrcodecode(channel.getAislecode());
			income.setQrcodename(channel.getName());
			income.setQrcodeuserid(channel.getUserid());
			income.setDynamic(false);
			// 随机数
			income.setAmount(Double.valueOf(qs.getPay_amount()));
			if (mc.getClearingtype())
				income.setRealamount(Double.valueOf(StringUtil.getDouble(qs.getPay_amount())));
			else
				income.setRealamount(income.getAmount());
			income.setStatus(DictionaryResource.PAYOUTSTATUS_50);
			income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
			income.setNotifyurl(qs.getPay_notifyurl());
			income.setBackforwardurl(qs.getPay_callbackurl());
			income.setInipaddress(AuthContext.getIp());
			switch (channel.getName()) {
			case DictionaryResource.ONEPLUSAISLE:
				JSONObject dataoneplus = PayUtil.SendOnePlusSubmit(income, channel);
				if (dataoneplus != null) {
					flage = false;
					income.setResulturl(dataoneplus.getStr("payData"));
					income.setQrcodeordernum(dataoneplus.getStr("payOrderId"));
				}
				break;
			case DictionaryResource.TONGYUSNAISLE:
				JSONObject data = PayUtil.SendTongYuanSubmit(income, channel);
				if (data != null) {
					flage = false;
					income.setResulturl(data.getStr("payData"));
					income.setQrcodeordernum(data.getStr("payOrderId"));
				}
				break;
			case DictionaryResource.ZSAISLE:
				JSONObject zsjz = PayUtil.SendZSSubmit(income, channel);
				if (zsjz != null) {
					flage = false;
					income.setResulturl(zsjz.getJSONObject("PayeeInfo").getStr("CashUrl"));
					income.setQrcodeordernum(zsjz.getStr("OrderNo"));
				}
				break;
			case DictionaryResource.XSAISLE:
				SysXSOrder xsjz = PayUtil.SendXSSubmit(income, channel);
				if (xsjz != null) {
					flage = false;
					income.setResulturl(xsjz.getResult().getQrCode());
					income.setQrcodeordernum(xsjz.getResult().getOrderCode());
				}
				break;
			case DictionaryResource.YSAISLE:
				SysYSOrder syjz = PayUtil.SendYSSubmit(income, channel);
				if (syjz != null) {
					flage = false;
					income.setResulturl(syjz.getPayParams().getPayUrl());
					income.setQrcodeordernum(syjz.getPayOrderId());
				}
				break;
			case DictionaryResource.FHLAISLE:
				SysFhOrder sfh = PayUtil.SendFhSubmit(income, channel);
				if (sfh != null) {
					flage = false;
					income.setResulturl(sfh.getPayParams().getPayJumpUrl());
					income.setQrcodeordernum(sfh.getPayOrderId());
				}
				break;
			case DictionaryResource.EGAISLE:
				SysTdOrder seg = PayUtil.SendEgSubmit(income, channel);
				if (seg != null) {
					flage = false;
					income.setResulturl(seg.getData().getPay_url());
					income.setQrcodeordernum(income.getMerchantordernum());
				}
				break;
			case DictionaryResource.KFAISLE:
				SysHsOrder sho = PayUtil.SendKFSubmit(income, channel);
				if (sho != null) {
					flage = false;
					income.setResulturl(sho.getPay_url());
					income.setQrcodeordernum(sho.getSys_order_no());
				}
				break;
			case DictionaryResource.WDAISLE:
				SysWdOrder wd = PayUtil.SendWdSubmit(income, channel);
				if (wd != null) {
					flage = false;
					income.setResulturl(wd.getData().getPayData());
					income.setQrcodeordernum(wd.getData().getPayOrderId());
				}
				break;
			case DictionaryResource.RBLAISLE:
				SysRblOrder rbl = PayUtil.SendRblSubmit(income, channel);
				if (rbl != null) {
					flage = false;
					income.setResulturl(rbl.getData().getPayUrl());
					income.setQrcodeordernum(rbl.getData().getTradeNo());
				}
				break;
			case DictionaryResource.GZAISLE:
				SysGzOrder gz = PayUtil.SendGzSubmit(income, channel);
				if (gz != null) {
					flage = false;
					income.setResulturl(gz.getData().getPayUrl());
					income.setQrcodeordernum(income.getMerchantordernum());
				}
				break;
			case DictionaryResource.WJAISLE:
				SysWjOrder wj = PayUtil.SendWjSubmit(income, channel);
				if (wj != null) {
					flage = false;
					income.setResulturl(wj.getData().getPayData());
					income.setQrcodeordernum(wj.getData().getPayOrderId());
				}
				break;
			case DictionaryResource.FCAISLE:
				SysFcOrder fc = PayUtil.SendFcSubmit(income, channel);
				if (fc != null) {
					flage = false;
					income.setResulturl(fc.getData().getPayUrl());
					income.setQrcodeordernum(fc.getData().getTradeNo());
				}
				break;
			case DictionaryResource.AKLAISLE:
				SysFcOrder akl = PayUtil.SendAklSubmit(income, channel);
				if (akl != null) {
					flage = false;
					income.setResulturl(akl.getData().getPayUrl());
					income.setQrcodeordernum(akl.getData().getTradeNo());
				}
				break;
			}
			if (flage) {
				channelbot.notifyChannel(channel);
				QrcodeResultVO qr = new QrcodeResultVO();
				qr.setPay_memberid(mc.getCode());
				qr.setPay_orderid(qs.getPay_orderid());
				qr.setPay_amount(qs.getPay_amount());
				qr.setPay_aislecode(income.getOrdernum());
				qr.setPay_viewurl(appConfig.getOrigin() + "/esay/rest/v1/view/income/error");
				String signresult = PayUtil.SignMd5ResultQrocde(qr, mc.getAppkey());
				qr.setPay_md5sign(signresult);
				return qr;
			}
			if (mc.getAgentid() != null) {
				Agent ag = agentmapper.get(mc.getAgentid());
				income.setAgentid(ag.getId());
				Agentaccountorder aat = new Agentaccountorder();
				aat.setAgentid(ag.getId());
				aat.setUserid(ag.getUserid());
				aat.setUsername(ag.getName());
				aat.setNkname(ag.getNkname());
				aat.setStatus(DictionaryResource.PAYOUTSTATUS_50);
				aat.setExchange(ag.getExchange());
				aat.setAmount(income.getAmount());// 金额
				aat.setDeal(income.getAmount() * (ag.getExchange() / 100));// 交易费
				aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
				aat.setOnecost(ag.getOnecost());// 手续费
				aat.setType(DictionaryResource.ORDERTYPE_20.toString());
				aat.setOrdernum("ina" + StringUtil.getOrderNum());
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
			income.setIncomeamount(Double.valueOf(String.format("%.2f", (income.getMerchantincomeamount() - income.getChannelincomeamount() - income.getAgentincome()))));
			// 商户收入
			income.setMerchantincomeamount(Double.valueOf(String.format("%.2f", (income.getAmount() - income.getMerchantincomeamount()))));
			// 计算当前码可生成的订单
			income.setFewamount(0.00);
			income.setCollection(mc.getCollection());
			income.setExchange(channel.getCollection());
			income.setType(DictionaryResource.ORDERTYPE_27.toString());
			income.setRemark("新增代收资金￥：" + income.getAmount());
			int i = mapper.post(income);
			if (i == 0) {
				throw new YtException("当前系統繁忙");
			}
			QrcodeResultVO result = addOtherOrder(income, channel, aisle, mc, qs);
			TenantIdContext.remove();
			return result;
		} else {
			//////////////////////////////////////////////////////////////////////////////////////// 原生支付原生支付原生支付原生支付原生支付
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
			// 小于设置限额
			List<Qrcode> listcmm = listqrcode.stream().filter(c -> c.getMax() >= amount && c.getMin() <= amount && (c.getTodayincome() + amount) < c.getLimits() && c.getStatus()).collect(Collectors.toList());
			Assert.notEmpty(listcmm, "代收金额超出限额");
			List<Qrcode> listcf = listcmm.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
			Qrcode qd = null;
			if (listcf.size() > 0) {
				for (int j = 0; j < listcf.size(); j++) {
					Qrcode qc = listcf.get(j);
					String[] match = qc.getFirstmatchmoney().split(",");
					for (int i = 0; i < match.length; i++) {
						String number = match[i];
						if (number.indexOf("-") == -1 && (Integer.valueOf(amount.toString()) == Integer.valueOf(number))) {
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
			// 支付通手机H5
			if (qd.getCode().equals(DictionaryResource.PRODUCT_ZFTWAP)) {
				Qrcode pqd = qrcodemapper.get(qd.getPid());
				AlipayTradeWapPayResponse response = SelfPayUtil.AlipayTradeWapPay(pqd, qd, income.getOrdernum(), income.getAmount());
				if (response != null) {
					flage = false;
					String pageRedirectionData = response.getBody();
					income.setQrcode(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
					income.setQrcodeordernum("inqd" + StringUtil.getOrderNum());
					income.setResulturl(pageRedirectionData);
					if (qd.getPid() != null) {
						income.setDynamic(true);
					}
				}
			} else if (qd.getCode().equals(DictionaryResource.PRODUCT_YPLWAP)) {
				flage = false;
				income.setQrcode(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
				income.setResulturl(income.getQrcode());
				income.setQrcodeordernum("inqd" + StringUtil.getOrderNum());
			} else if (qd.getCode().equals(DictionaryResource.PRODUCT_HUIFUTXWAP)) {
				Map<String, Object> response = SelfPayUtil.quickbuckle(qd, income.getOrdernum(), income.getAmount(), income.getBackforwardurl());
				if (response != null) {
					flage = false;
					String outradeno = response.get("hf_seq_id").toString();
					String form_url = response.get("form_url").toString();
					income.setQrcode(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
					income.setResulturl(form_url);
					income.setQrcodeordernum(outradeno);
				}
			}
			if (flage) {
				channelbot.notifyChannel(channel);
				QrcodeResultVO qr = new QrcodeResultVO();
				qr.setPay_memberid(mc.getCode());
				qr.setPay_orderid(qs.getPay_orderid());
				qr.setPay_amount(qs.getPay_amount());
				qr.setPay_aislecode(income.getOrdernum());
				qr.setPay_viewurl(appConfig.getOrigin() + "/esay/rest/v1/view/income/error");
				String signresult = PayUtil.SignMd5ResultQrocde(qr, mc.getAppkey());
				qr.setPay_md5sign(signresult);
				return qr;
			}
			///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
			if (mc.getAgentid() != null) {
				Agent ag = agentmapper.get(mc.getAgentid());
				income.setAgentid(ag.getId());
				Agentaccountorder aat = new Agentaccountorder();
				aat.setAgentid(ag.getId());
				aat.setUserid(ag.getUserid());
				aat.setUsername(ag.getName());
				aat.setNkname(ag.getNkname());
				aat.setStatus(DictionaryResource.PAYOUTSTATUS_50);
				aat.setExchange(ag.getExchange());
				aat.setAmount(income.getAmount());// 金额
				aat.setDeal(income.getAmount() * (ag.getExchange() / 100));// 交易费
				aat.setAmountreceived(aat.getDeal() + ag.getOnecost());// 总费用
				aat.setOnecost(ag.getOnecost());// 手续费
				aat.setType(DictionaryResource.ORDERTYPE_20.toString());
				aat.setOrdernum("ina" + StringUtil.getOrderNum());
				aat.setRemark("代收资金￥：" + aat.getAmount() + " 交易费：" + String.format("%.2f", aat.getDeal()) + " 手续费：" + aat.getOnecost());
				income.setAgentincome(aat.getAmountreceived());
				income.setAgentordernum(aat.getOrdernum());
				agentaccountordermapper.post(aat);
				agentaccountservice.totalincome(aat);
			} else {
				income.setAgentincome(0.00);
			}
			// 渠道收入
			income.setChannelincomeamount(NumberUtil.multiply(income.getAmount().toString(), (qd.getCollection() / 100) + "", 2).doubleValue());
			// 商户收入
			income.setMerchantincomeamount(NumberUtil.multiply(income.getAmount().toString(), (mc.getCollection() / 100) + "", 2).doubleValue());
			// 系统收入
			income.setIncomeamount(Double.valueOf(String.format("%.2f", (income.getMerchantincomeamount() - income.getChannelincomeamount() - income.getAgentincome()))));
			// 商户收入
			income.setMerchantincomeamount(Double.valueOf(String.format("%.2f", (income.getAmount() - income.getMerchantincomeamount()))));
			// 计算当前码可生成的订单
			income.setFewamount(0.00);
			income.setCollection(mc.getCollection());
			income.setExchange(qd.getCollection());
			income.setInipaddress(AuthContext.getIp());
			income.setType(DictionaryResource.ORDERTYPE_27.toString());
			income.setRemark("新增代收资金￥：" + income.getAmount());
			int i = mapper.post(income);
			if (i == 0) {
				throw new YtException("当前系統繁忙");
			}
			QrcodeResultVO result = addOtherOrderMqd(income, channel, qas, mc, qs);
			TenantIdContext.remove();
			return result;
		}
	}

	@Override
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
			throw new YtException("非法请求,IP加白名单后重试!");
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
		log.info(ip);
		if (mc.getIpstatus()) {
			if (ip == null || ip.equals("")) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
			if (mc.getIpaddress() == null || mc.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
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

	private Income addIncome(Channel channel, Qrcodeaisle qas, Merchant mc, QrcodeSubmitDTO qs, Qrcode qd) {
		Income income = new Income();
		// 商戶
		income.setMerchantuserid(mc.getUserid());
		income.setOrdernum("in" + StringUtil.getOrderNum());
		income.setMerchantorderid(qs.getPay_orderid());
		income.setMerchantordernum("inm" + qs.getPay_orderid());
		income.setMerchantcode(mc.getCode());
		income.setMerchantname(mc.getName());
		income.setMerchantid(mc.getId());
		income.setExpireddate(DateTimeUtil.addMinute(qd.getExpireminute()));
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
		income.setDynamic(false);
		// 随机数
		income.setAmount(Double.valueOf(qs.getPay_amount()));
		if (mc.getClearingtype())
			income.setRealamount(Double.valueOf(StringUtil.getDouble(qs.getPay_amount())));
		else
			income.setRealamount(income.getAmount());

		income.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
		income.setNotifyurl(qs.getPay_notifyurl());
		income.setBackforwardurl(qs.getPay_callbackurl());
		return income;
	}

	// 上游渠道下单
	private QrcodeResultVO addOtherOrder(Income income, Channel channel, Aisle qas, Merchant mc, QrcodeSubmitDTO qs) {
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
		qao.setCollection(income.getExchange());
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
		imao.setCollection(income.getCollection());
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
		qr.setPay_aislecode(income.getOrdernum());
		qr.setPay_viewurl(income.getResulturl());
		String signresult = PayUtil.SignMd5ResultQrocde(qr, mc.getAppkey());
		qr.setPay_md5sign(signresult);
		return qr;
	}

	// 添加qrcode订单
	private QrcodeResultVO addOtherOrderMqd(Income income, Channel channel, Qrcodeaisle qas, Merchant mc, QrcodeSubmitDTO qs) {
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
		qao.setCollection(income.getExchange());
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
		imao.setCollection(income.getCollection());
		imao.setExpireddate(income.getExpireddate());
		imao.setIncomeamount(income.getMerchantincomeamount());
		incomemerchantaccountordermapper.post(imao);
		incomemerchantaccountservice.totalincome(imao);

		QrcodeResultVO qr = new QrcodeResultVO();
		qr.setPay_memberid(mc.getCode());
		qr.setPay_orderid(qs.getPay_orderid());
		qr.setPay_amount(qs.getPay_amount());
		qr.setPay_aislecode(income.getOrdernum());
		qr.setPay_viewurl(income.getQrcode());
		String signresult = PayUtil.SignMd5ResultQrocde(qr, mc.getAppkey());
		qr.setPay_md5sign(signresult);
		return qr;
	}

	private void success(Income income) {
		// 計算代收
		income.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		if (income.getNotifystatus() == DictionaryResource.PAYOUTNOTIFYSTATUS_61)
			income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
		income.setSuccesstime(DateTimeUtil.getNow());
		income.setBacklong(DateUtil.between(income.getSuccesstime(), income.getCreate_time(), DateUnit.SECOND));
		//
		income.setRemark("成功代收资金￥：" + income.getAmount());
		mapper.put(income);

		// 计算代理
		if (income.getAgentid() != null) {
			Agentaccountorder aao = agentaccountordermapper.getByOrdernum(income.getAgentordernum());
			aao.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			// 代理订单
			agentaccountordermapper.put(aao);
			// 代理账户
			agentaccountservice.updateTotalincome(aao);
		}

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

		// 计算系统收入
		systemaccountservice.updateIncome(income);
	}

	// 自营三方回调
	private void success(Income income, String trade_no) {
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			// 渠道
			Qrcodeaccountorder qrcodeaccountorder = qrcodeaccountordermapper.getByOrderNum(income.getQrcodeordernum());
			qrcodeaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			if (trade_no != null)
				qrcodeaccountorder.setOrdernum(trade_no);
			qrcodeaccountordermapper.put(qrcodeaccountorder);
			// 計算代收
			income.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			if (trade_no != null)
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
			}
			income.setRemark("成功代收资金￥：" + income.getAmount());
			//
			mapper.put(income);
			// 计算渠道收入
			qrcodeaccountservice.updateTotalincome(qrcodeaccountorder);
			// 计算碼商收入金额
			qrcodeservice.updateTotalincome(qrcodeaccountorder);
			//
			Incomemerchantaccountorder incomemerchantaccountorder = incomemerchantaccountordermapper.getByOrderNum(income.getMerchantorderid());
			incomemerchantaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			incomemerchantaccountordermapper.put(incomemerchantaccountorder);
			// 计算商户收入
			incomemerchantaccountservice.updateTotalincome(incomemerchantaccountorder);
			// 计算系统收入
			systemaccountservice.updateIncome(income);
		}

	}

	@Override
	@Transactional
	public Integer notify(Income income) {
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
		return mapper.put(income);
	}

	@Override
	@Transactional
	public void successstatus(Income income) {
		Income newincome = mapper.get(income.getId());
		success(newincome);
	}

	@Override
	public Integer addblock(Income income) {
		Income newincome = mapper.get(income.getId());
		Blocklist bl = new Blocklist();
		bl.setMerchantid(newincome.getMerchantid());
		bl.setHexaddress(newincome.getBlockaddress());
		bl.setIpaddress(newincome.getInipaddress());
		bl.setOrdernum(newincome.getOrdernum());
		Integer i = blocklistmapper.post(bl);
		income.setBlockaddress("");
		mapper.updateBlock(income);
		return i;
	}

	@Override
	public Integer settleconfirm(Income income) {
		Income in = mapper.get(income.getId());
		Integer i = 0;
		if (in.getDynamic()) {
			Qrcode qd = qrcodemapper.get(in.getQrcodeid());
			Qrcode pqd = qrcodemapper.get(qd.getPid());
			AlipayTradeSettleConfirmResponse atsc = SelfPayUtil.AlipayTradeSettleConfirm(pqd, in.getQrcodeordernum(), in.getAmount());
			Assert.notNull(atsc, "结算失败!");
			in.setStatus(DictionaryResource.PAYOUTSTATUS_54);
			mapper.put(in);
		}
		return i;
	}

	@Override
	public Integer batchsettleconfirm(Map<String, Object> param) {
		param.put("dir", "Asc");
		List<IncomeVO> list = mapper.page(param);
		Integer i = 0;
		list.forEach(in -> {
			Qrcode qd = qrcodemapper.get(in.getQrcodeid());
			Qrcode pqd = qrcodemapper.get(qd.getPid());
			AlipayTradeSettleConfirmResponse atsc = SelfPayUtil.AlipayTradeSettleConfirm(pqd, in.getQrcodeordernum(), in.getAmount());
			Assert.notNull(atsc, "结算失败!");
			in.setStatus(DictionaryResource.PAYOUTSTATUS_54);
			mapper.put(in);
			in.setVersion(in.getVersion() + 1);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			AlipayTradeOrderSettleResponse atos = SelfPayUtil.AlipayTradeOrderSettle(pqd, in.getQrcodeordernum(), "li1850420@sina.com", in.getAmount() * 0.01);
			Assert.notNull(atos, "分账失败!");
			in.setStatus(DictionaryResource.PAYOUTSTATUS_55);
			mapper.put(in);
		});
		return i;
	}

	@Override
	public void sumbmitcheck(Map<String, Object> params) {
		Income in = mapper.getByOrderNum(params.get("orderid").toString());
		TenantIdContext.setTenantId(in.getTenant_id());
		log.info(params.get("orderid").toString());
		Qrcode qrcode = qrcodemapper.get(in.getQrcodeid());
		String smsno = SelfPayUtil.eplpayTradeWapPay(qrcode, in.getOrdernum(), in.getAmount(), params.get("name").toString(), params.get("pcardno").toString(), params.get("cardno").toString(), params.get("mobile").toString());
		Assert.notNull(smsno, "易票联下单失败");
		Qrcodpaymember qrcodpaymember = new Qrcodpaymember();
		qrcodpaymember.setQrcodeid(qrcode.getId());
		qrcodpaymember.setQrcodename(qrcode.getName());
		qrcodpaymember.setMobile(params.get("mobile").toString());
		qrcodpaymember.setPcardno(params.get("pcardno").toString());
		qrcodpaymember.setName(params.get("name").toString());
		qrcodpaymember.setCardno(params.get("cardno").toString());
		qrcodpaymember.setSmsno(smsno);
		qrcodpaymember.setMemberid(in.getOrdernum());
		qrcodpaymembermapper.post(qrcodpaymember);
		TenantIdContext.remove();
	}

	@Override
	public void sumbmitcpay(Map<String, Object> params) {
		Income in = mapper.getByOrderNum(params.get("orderid").toString());
		Qrcodpaymember qrcodpaymember = qrcodpaymembermapper.getByMermberId(params.get("orderid").toString());
		log.info(params.get("orderid").toString());
		String outTradeNo = SelfPayUtil.eplprotocolPayPre(qrcodemapper.get(in.getQrcodeid()), in.getOrdernum(), in.getQrcodeordernum(), qrcodpaymember.getSmsno(), params.get("smscode").toString(),
				Long.valueOf(String.format("%.2f", in.getAmount()).replace(".", "")));
		Assert.notNull(outTradeNo, "易票联支付失败");
	}

	@Override
	public void epfpayftfcallback(Map<String, Object> params) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(params);
		String outradeno = params.get("outTradeNo").toString();
		String noncestr = params.get("nonceStr").toString();
		log.info("易票联通知返回消息：outradeno" + outradeno + " noncestr:" + noncestr);
		Income income = mapper.getByQrcodeOrderNum(outradeno);
		TenantIdContext.setTenantId(income.getTenant_id());
		String returnstate = SelfPayUtil.eplpaymentQuery(qrcodemapper.get(income.getQrcodeid()), outradeno, noncestr);
		Assert.notNull(returnstate, "易票联通知反查订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			success(income, null);
		}
		TenantIdContext.remove();
	}

	@Override
	public String huifupayftfcallback(Map<String, String> params) {
		if (params.get("resp_code").toString().equals("00000000")) {
			String outradeno = JSONUtil.parseObj(params.get("resp_data").toString()).getStr("req_seq_id");
			log.info("汇付通知返回消息：payOrderId" + outradeno);
			Income income = mapper.getByOrderNum(outradeno);
			TenantIdContext.setTenantId(income.getTenant_id());
			String response = SelfPayUtil.huifupaymentQuery(qrcodemapper.get(income.getQrcodeid()), outradeno);
			Assert.notNull(response, "汇付通知反查订单失败!");
			if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
				success(income, null);
			}
			TenantIdContext.remove();
			return income.getOrdernum();
		}
		return null;
	}

}