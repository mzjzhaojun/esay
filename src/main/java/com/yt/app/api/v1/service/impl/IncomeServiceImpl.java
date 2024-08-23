package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantqrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

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
 * @version v1 @createdate2024-08-22 23:02:54
 */

@Service
public class IncomeServiceImpl extends YtBaseServiceImpl<Income, Long> implements IncomeService {

	@Autowired
	YtConfig appConfig;

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

	@Override
	@Transactional
	public Integer post(Income t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Income> list(Map<String, Object> param) {
		List<Income> list = mapper.list(param);
		return new YtPageBean<Income>(list);
	}

	@Override
	public Income get(Long id) {
		Income t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<IncomeVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomeVO>(Collections.emptyList());
		}
		List<IncomeVO> list = mapper.page(param);
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
		String sign = PayUtil.SignMd5Qrocde(qs, mc.getAppkey());
		System.out.println(sign);
		if (!sign.equals(qs.getPay_md5sign())) {
			throw new YtException("签名不正确!");
		}
		List<Merchantqrcodeaisle> listmc = merchantqrcodeaislemapper.getByMid(mc.getId());
		if (listmc == null || listmc.size() == 0) {
			throw new YtException("商戶还沒有配置通道!");
		}
		long[] qraids = listmc.stream().mapToLong(qa -> qa.getQrcodeaisleid()).distinct().toArray();
		List<Qrcodeaisle> listqa = qrcodeaislemapper.listByArrayId(qraids);
		Qrcodeaisle qas = listqa.stream().filter(qa -> qa.getCode().equals(qs.getPay_aislecode())).findFirst().get();
		if (qas == null) {
			throw new YtException("通道不匹配");
		}

		List<Qrcodeaisleqrcode> listqaq = qrcodeaisleqrcodemapper.getByQrcodeAisleId(qas.getId());
		long[] qaqids = listqaq.stream().mapToLong(qaq -> qaq.getQrcodelid()).distinct().toArray();
		List<Qrcode> listqrcode = qrcodemapper.listByArrayId(qaqids);
		Integer amount = Integer.parseInt(qs.getPay_amount());
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
		System.out.println("====================" + qd.getName());

		Income income = new Income();
		// 商戶
		income.setMerchantuserid(mc.getUserid());
		income.setOrdernum(StringUtil.getOrderNum());
		income.setMerchantorderid(qs.getPay_orderid());
		income.setMerchantordernum("IM" + StringUtil.getOrderNum());
		income.setMerchantcode(mc.getCode());
		income.setMerchantname(mc.getName());
		income.setMerchantid(mc.getId());
		income.setMerchantpay(Double.valueOf(qs.getPay_amount()));
		// 通道
		income.setAislecode(qas.getCode());
		income.setQrcodeaisleid(qas.getId());
		income.setQrcodeaislename(qas.getName());
		// 收款码
		income.setQrcodeuserid(qd.getUserid());
		income.setQrcodeid(qd.getId());
		income.setQrcodename(qd.getName());
		income.setQrcodeordernum("IC" + StringUtil.getOrderNum());
		income.setAmount(Double.valueOf(qs.getPay_amount()));

		income.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);
		income.setNotifyurl(qs.getPay_notifyurl());
		income.setResulturl(qs.getPay_callbackurl());
		income.setQrcode(qd.getFixedcode());
		income.setResulturl(appConfig.getViewurl().replace("{id}", income.getOrdernum() + ""));
		// 计算当前码可生成的订单
		RLock lock = RedissonUtil.getLock(qd.getId());
		Integer i = 0;
		try {
			lock.lock();
			Double fewamount = getFewAmount(qd.getId());
			if (fewamount < 3) {
				income.setFewamount(fewamount);
				income.setRealamount(income.getAmount() - fewamount);
				income.setType(qd.getType());
				i = mapper.post(income);
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		if (i == 0) {
			throw new YtException("当前通道码繁忙");
		}

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

	public Double getFewAmount(Long qid) {
		Double min = 0.01;
		for (int i = 1; i <= 20; i++) {
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
	public QrcodeResultVO queryqrcode(QrcodeSubmitDTO qs) {

		return null;
	}
}