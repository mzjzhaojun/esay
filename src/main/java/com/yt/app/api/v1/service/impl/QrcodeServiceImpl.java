package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodestatisticalreportsMapper;
import com.yt.app.api.v1.mapper.QrcodetransferrecordMapper;
import com.yt.app.api.v1.service.QrcodeService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.entity.Qrcodestatisticalreports;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.api.v1.vo.QrcodeVO;
import com.yt.app.api.v1.vo.QrcodeaccountorderVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.SelfPayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Slf4j
@Service
public class QrcodeServiceImpl extends YtBaseServiceImpl<Qrcode, Long> implements QrcodeService {

	@Autowired
	private QrcodeMapper mapper;

	@Autowired
	private IncomeMapper incomemapper;

	@Autowired
	private QrcodeMapper qrcodemapper;

	@Autowired
	private QrcodeaisleqrcodeMapper qrcodeaisleqrcodemapper;

	@Autowired
	private QrcodestatisticalreportsMapper qrcodestatisticalreportsmapper;

	@Autowired
	private QrcodeaccountorderMapper qrcodeaccountordermapper;

	@Autowired
	private QrcodetransferrecordMapper qrcodetransferrecordmapper;

	@Override
	@Transactional
	public Integer post(Qrcode t) {
		t.setUserid(SysUserContext.getUserId());
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcode get(Long id) {
		Qrcode t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		// 删除关联表
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		qrcodeaisleqrcodemapper.deleteByQrcodelId(id);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeVO> page(Map<String, Object> param) {

		if (param.get("qrcodeaisleid") != null) {
			List<Qrcodeaisleqrcode> listmqas = qrcodeaisleqrcodemapper.getByQrcodeAisleId(Long.valueOf(param.get("qrcodeaisleid").toString()));
			if (listmqas.size() > 0) {
				long[] qraids = listmqas.stream().mapToLong(mqa -> mqa.getQrcodelid()).distinct().toArray();
				param.put("existids", qraids);
			}
		}

		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeVO>(Collections.emptyList());
		}
		List<QrcodeVO> list = mapper.page(param);
		list.forEach(p -> {
			p.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + p.getType()));
		});
		return new YtPageBean<QrcodeVO>(param, list, count);
	}

	@Override
	public QrcodeVO paytestzft(Qrcode qrcode) {
		Qrcode pqrcode = mapper.get(qrcode.getPid());
		AlipayTradeWapPayResponse atp = SelfPayUtil.AlipayTradeWapPay(pqrcode, qrcode, StringUtil.getOrderNum(), qrcode.getBalance());
		Assert.notNull(atp, "获取支付宝单号错误!");
		QrcodeVO qrv = new QrcodeVO();
		qrv.setPayurl(atp.getBody());
		return qrv;
	}

	@Override
	public String paytestepl(Map<String, Object> params) {
		Qrcode pqrcode = mapper.get(Long.valueOf(params.get("id").toString()));
		String atp = SelfPayUtil.eplpayTradeWapPay(pqrcode, params.get("menmberid").toString(), Double.valueOf(params.get("amount").toString()), params.get("name").toString(), params.get("pcardno").toString(), params.get("cardno").toString(),
				params.get("mobile").toString());
		Assert.notNull(atp, "获取易票联单号错误!");
		return atp;
	}

	@Override
	public String paytesteplcafrom(Map<String, Object> params) {
		Qrcode pqrcode = mapper.get(Long.valueOf(params.get("id").toString()));
		System.out.println(params.get("smsno").toString() + "ceee" + params.get("smscode").toString());
		String atp = SelfPayUtil.eplprotocolPayPre(pqrcode, StringUtil.getOrderNum(), params.get("menmberid").toString(), params.get("smsno").toString(), params.get("smscode").toString(),
				Long.valueOf(String.format("%.2f", params.get("amount")).replace(".", "")));
		Assert.notNull(atp, "易票联支付错误!");
		return atp;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void scuccessTotalincome(Qrcodeaccountorder qo) {
		Qrcode m = mapper.get(qo.getQrcodeid());
		m.setTodayorder(m.getTodayorder() + 1);
		m.setOrdersum(m.getOrdersum() + 1);
		m.setTodayincome(m.getTodayincome() + qo.getAmount());
		m.setIncomesum(m.getIncomesum() + qo.getAmount());
		m.setTodaybalance(m.getTodaybalance() + qo.getIncomeamount());
		mapper.put(m);
	}

	@Override
	public void updateTotalincome(Qrcodeaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getQrcodeid());
		try {
			lock.lock();
			scuccessTotalincome(mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void updateDayValue(Qrcode c, String date) {
		RLock lock = RedissonUtil.getLock(c.getId());
		try {
			lock.lock();
			TenantIdContext.setTenantId(c.getTenant_id());
			// 插入报表数据
			Qrcodestatisticalreports csr = new Qrcodestatisticalreports();
			csr.setDateval(date);
			csr.setName(c.getName());
			csr.setBalance(c.getBalance());
			csr.setUserid(c.getUserid());
			csr.setQrcodeid(c.getId());
			csr.setTodayincome(c.getTodayincome());
			csr.setIncomecount(c.getIncomesum());
			// 查询每日统计数据
			// 统计码商
			QrcodeaccountorderVO imaov = qrcodeaccountordermapper.countOrder(c.getId(), date);
			csr.setTodayorder(imaov.getOrdercount());
			csr.setTodayorderamount(imaov.getAmount());
			csr.setTodaysuccessorderamount(imaov.getIncomeamount());
			// 统计码商
			QrcodeaccountorderVO imaovsuccess = qrcodeaccountordermapper.countSuccessOrder(c.getId(), date);
			csr.setSuccessorder(imaovsuccess.getOrdercount());
			csr.setIncomeuserpaycount(imaovsuccess.getAmount());
			csr.setIncomeuserpaysuccesscount(imaovsuccess.getIncomeamount());

			try {
				if (csr.getSuccessorder() > 0) {
					double successRate = ((double) csr.getSuccessorder() / csr.getTodayorder()) * 100;
					csr.setPayoutrate(successRate);
				}
			} catch (Exception e) {
				csr.setPayoutrate(0.0);
			}
			qrcodestatisticalreportsmapper.post(csr);
			// 清空每日数据
			mapper.updatetodayvalue(c);
			TenantIdContext.remove();
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void zftaccountquery(Qrcode qrcode) {
		AlipayFundAccountQueryResponse afaqr = SelfPayUtil.AlipayFundAccountQuery(qrcode);
		qrcode.setBalance(Double.valueOf(afaqr.getAvailableAmount()));
		qrcode.setFreezebalance(Double.valueOf(afaqr.getFreezeAmount()));
		mapper.put(qrcode);
	}

	@Override
	public void transunitransfer(Qrcodetransferrecord qtc) {
		Qrcode qrcode = mapper.get(qtc.getQrcodeid());
		qtc.setOutbizno(StringUtil.getOrderNum());
		AlipayFundTransUniTransferResponse aftutr = SelfPayUtil.AlipayFundTransUniTransfer(qrcode, qtc);
		Assert.notNull(aftutr, "转账失败!");
		qtc.setOrdernum(aftutr.getOrderId());
		qtc.setStatus(DictionaryResource.ALIPAY_STATUS_701);
		qrcodetransferrecordmapper.post(qtc);
	}

	@Override
	public void eplaccountquery(Qrcode qrcode) {
		Qrcode pqrcode = mapper.get(qrcode.getId());
		JSONObject response = SelfPayUtil.eplaccountQuery(pqrcode);
		log.info(response.getInt("availableBalance").toString());
		String availableBalance = response.getInt("availableBalance").toString();
		String floatBalance = response.getInt("floatBalance").toString();
		if (availableBalance.equals("0")) {
			availableBalance = "0000";
		}
		if (floatBalance.equals("0")) {
			floatBalance = "0000";
		}
		pqrcode.setBalance(Double.valueOf(availableBalance.substring(0, availableBalance.length() - 2) + "." + availableBalance.substring(availableBalance.length() - 2)));
		pqrcode.setFreezebalance(Double.valueOf(floatBalance.substring(0, floatBalance.length() - 2) + "." + floatBalance.substring(floatBalance.length() - 2)));
		mapper.put(pqrcode);
	}

	@Override
	public void epltransunitransfer(Qrcodetransferrecord qrcode) {
		Qrcode pqrcode = mapper.get(qrcode.getId());
		String returndata = SelfPayUtil.eplwithdrawalToCard(pqrcode, qrcode.getOrdernum(), qrcode.getPayeename(), qrcode.getPayeeid(), qrcode.getBankname(), qrcode.getPayeetype(),
				Long.valueOf(String.format("%.2f", qrcode.getAmount()).replace(".", "")));
		Assert.notNull(returndata, "转账失败!");
	}

	@Override
	public void zfbtradeordersettle(Qrcodetransferrecord c) {
		Income in = incomemapper.get(c.getQrcodeid());
		if (in.getDynamic()) {
			Qrcode qd = qrcodemapper.get(in.getQrcodeid());
			Qrcode pqd = qrcodemapper.get(qd.getPid());
			AlipayTradeOrderSettleResponse atsc = SelfPayUtil.AlipayTradeOrderSettle(pqd, in.getQrcodeordernum(), c.getPayeeid(), c.getAmount());
			Assert.notNull(atsc, "分账失败!");
			in.setStatus(DictionaryResource.PAYOUTSTATUS_55);
			incomemapper.put(in);
			c.setOutbizno(in.getOrdernum());
			c.setOrdernum(in.getQrcodeordernum());
			c.setStatus(DictionaryResource.ALIPAY_STATUS_702);
			qrcodetransferrecordmapper.post(c);
		}
	}
}