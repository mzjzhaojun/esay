package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantqrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
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
	@Transactional
	public QrcodeResultVO submitQrcode(QrcodeSubmitDTO qs) {

		// 验证
		if (qs.getPay_memberid().length() > 10) {
			throw new YtException("商户号错误!");
		}

		if (Double.valueOf(qs.getPay_amount()) < 10) {
			throw new YtException("支付金额错误!");
		}
		// 盘口商户
		Merchant mc = merchantmapper.getByCode(qs.getPay_memberid());

		if (mc == null) {
			throw new YtException("商户不存在!");
		}
		TenantIdContext.setTenantId(mc.getTenant_id());
		if (!mc.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		String sign = PayUtil.SignMd5SubmitQrocde(qs, mc.getAppkey());
		log.info(sign);
		if (!sign.equals(qs.getPay_md5sign())) {
			throw new YtException("签名不正确!");
		}
		Income income = mapper.getByMerchantOrderNum(qs.getPay_orderid());
		if (income != null) {
			throw new YtException("已经存在订单!");
		}
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
		Merchantqrcodeaisle mqd = listmc.stream().filter(mqds -> mqds.getQrcodeaisleid().equals(qas.getId()))
				.findFirst().get();
		List<Qrcodeaisleqrcode> listqaq = qrcodeaisleqrcodemapper.getByQrcodeAisleId(qas.getId());
		long[] qaqids = listqaq.stream().mapToLong(qaq -> qaq.getQrcodelid()).distinct().toArray();
		List<Qrcode> listqrcode = qrcodemapper.listByArrayId(qaqids);
		Double amount = Double.valueOf(qs.getPay_amount());
		List<Qrcode> listcmm = listqrcode.stream().filter(c -> c.getMax() >= amount && c.getMin() <= amount)
				.collect(Collectors.toList());
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

		income = new Income();
		// 商戶
		income.setChannelid(channel.getId());
		income.setMerchantuserid(mc.getUserid());
		income.setOrdernum(StringUtil.getOrderNum());
		income.setMerchantorderid(qs.getPay_orderid());
		income.setMerchantordernum("IM" + StringUtil.getOrderNum());
		income.setMerchantcode(mc.getCode());
		income.setMerchantname(mc.getName());
		income.setMerchantid(mc.getId());
		income.setExpireddate(DateTimeUtil.addMinute(qd.getExpireminute() + 3));// 多加3分钟
		// 通道
		income.setExpiredminute(qd.getExpireminute());
		income.setQrcodeaisleid(qas.getId());
		income.setQrcodeaislename(qas.getName());
		income.setQrcodeaislecode(qas.getCode());
		// 收款码
		income.setQrcodeuserid(qd.getUserid());
		income.setQrcodeid(qd.getId());
		income.setQrcodename(qd.getName());
		income.setAmount(Double.valueOf(qs.getPay_amount()));
		income.setDynamic(qd.getDynamic());

		income.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
		income.setNotifyurl(qs.getPay_notifyurl());
		income.setBackforwardurl(qs.getPay_callbackurl());
		income.setQrcode(qd.getFixedcode());
		// 动态码直接去线上拿连接
		if (qd.getDynamic()) {
			SysHsOrder sho = PayUtil.SendHSSubmit(income, channel);
			Assert.notNull(sho, "获取渠道订单失败!");
			income.setResulturl(sho.getPay_url());
			income.setQrcodeordernum(sho.getSys_order_no());
		} else {
			income.setResulturl(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
		}
		// 渠道收入
		income.setChannelincomeamount(
				NumberUtil.multiply(income.getAmount().toString(), "0." + channel.getCollection(), 2).doubleValue());
		income.setMerchantincomeamount(
				NumberUtil.multiply(income.getAmount().toString(), "0." + mqd.getCollection(), 2).doubleValue());
		// 系统收入
		income.setIncomeamount(Double
				.valueOf(String.format("%.2f", (income.getMerchantincomeamount() - income.getChannelincomeamount()))));
		// 商户收入
		income.setMerchantincomeamount(
				Double.valueOf(String.format("%.2f", (income.getAmount() - income.getMerchantincomeamount()))));
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
				Double fewamount = getFewAmount(qd.getId());
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

		// 添加qrcode订单
		Qrcodeaccountorder qao = new Qrcodeaccountorder();
		qao.setUserid(income.getQrcodeuserid());
		qao.setQrcodeaisleid(income.getQrcodeaisleid());
		qao.setQrcodeaislename(income.getQrcodeaislename());
		qao.setQrcodename(income.getQrcodename());
		qao.setQrcodeid(income.getQrcodeid());
		qao.setOrdernum(income.getQrcodeordernum());
		qao.setQrcodecode(qd.getCode());
		qao.setType(income.getType());
		qao.setFewamount(income.getFewamount());
		qao.setAmount(income.getChannelincomeamount());
		qao.setRealamount(income.getAmount());
		qao.setResulturl(income.getResulturl());
		qao.setMerchantname(income.getMerchantname());
		qao.setQrocde(income.getQrcode());
		qao.setDynamic(qd.getDynamic());
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
		imao.setQrcodecode(qd.getCode());
		imao.setDynamic(qd.getDynamic());
		imao.setType(income.getType());
		imao.setFewamount(income.getFewamount());
		imao.setAmount(income.getMerchantincomeamount());
		imao.setRealamount(income.getAmount());
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
		TenantIdContext.remove();
		return qr;
	}

	public Double getFewAmount(Long qid) {
		Double min = 0.01;
		for (int i = 1; i <= 30; i++) {
			String key = SystemConstant.CACHE_SYS_QRCODE + qid + "" + min;
			if (!RedisUtil.hasKey(key)) {
				RedisUtil.set(key, min.toString());
				return min;
			} else {
				min = min + 0.01;
			}
		}
		min = 3.33;
		return min;
	}

	@Override
	public QueryQrcodeResultVO queryqrcode(QrcodeSubmitDTO qs) {
		// 验证
		if (qs.getPay_memberid().length() > 10) {
			throw new YtException("商户号错误!");
		}
		// 盘口商户
		Merchant mc = merchantmapper.getByCode(qs.getPay_memberid());
		if (mc == null) {
			throw new YtException("商户不存在!");
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

	@Override
	public Income getByOrderNum(String ordernum) {
		return mapper.getByOrderNum(ordernum);
	}

	@Override
	public void hscallback(@RequestParam Map<String, String> params) {
		String orderid = params.get("orderid").toString();
		String status = params.get("status").toString();
		log.info("宏盛通知返回消息：orderid" + orderid + " status:" + status);
		Income income = mapper.getByOrderNum(orderid);
		TenantIdContext.setTenantId(income.getTenant_id());
		Channel channel = channelmapper.get(income.getChannelid());
		String returnstate = PayUtil.SendHSQuerySubmit(orderid, channel);
		Assert.notNull(returnstate, "宏盛获取渠道订单失败!");
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
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
			Incomemerchantaccountorder incomemerchantaccountorder = incomemerchantaccountordermapper
					.getByOrderNum(income.getMerchantorderid());
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
}