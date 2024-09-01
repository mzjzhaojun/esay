package com.yt.app.common.runtask;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.GetExchangeChannelOrderNumThread;
import com.yt.app.common.runnable.GetPayoutChannelOrderNumThread;
import com.yt.app.common.runnable.NotifyQrcodeIncomeThread;
import com.yt.app.common.runnable.NotifyTyPayoutThread;
import com.yt.app.common.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskConfig {

	@Autowired
	private IncomeMapper incomemapper;

	@Autowired
	private PayoutMapper payoutmapper;

	@Autowired
	private ExchangeMapper exchangemapper;

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Autowired
	private SysconfigService payconfigservice;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private QrcodeaccountorderMapper qrcodeaccountordermapper;

	@Autowired
	private IncomemerchantaccountorderMapper incomemerchantaccountordermapper;

	@Autowired
	private QrcodeaccountService qrcodeaccountservice;

	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;

	/**
	 * 更新实时汇率
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void getOKXExchange() throws InterruptedException {
		payconfigservice.initExchangeData();
	}

	/**
	 * 更新每日新数据
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "59 59 23 * * ?")
	public void updateTodayValue() throws InterruptedException {
		List<Merchant> listm = merchantmapper.list(new HashMap<String, Object>());
		listm.forEach(m -> {
			merchantmapper.updatetodayvalue(m.getId());
		});
		List<Tgmerchantgroup> listtmg = tgmerchantgroupmapper.list(new HashMap<String, Object>());
		listtmg.forEach(mg -> {
			tgmerchantgroupmapper.updatetodayvalue(mg.getId());
		});

		List<Tgchannelgroup> listtcg = tgchannelgroupmapper.list(new HashMap<String, Object>());
		listtcg.forEach(mg -> {
			tgchannelgroupmapper.updatetodayvalue(mg.getId());
		});
	}

	/**
	 * 查询代付需要通知
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void notifyPayout() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Payout> list = payoutmapper.selectNotifylist();
		for (Payout p : list) {
			log.info("通知ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_65);
			if (payoutmapper.put(p) > 0) {
				NotifyTyPayoutThread nf = new NotifyTyPayoutThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 查询代收需要通知
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void notifyIncome() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Income> list = incomemapper.selectNotifylist();
		for (Income p : list) {
			log.info("通知ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_65);
			if (incomemapper.put(p) > 0) {
				NotifyQrcodeIncomeThread nf = new NotifyQrcodeIncomeThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 代付线上获取单号
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void payout() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Payout> list = payoutmapper.selectAddlist();
		for (Payout p : list) {
			log.info("代付获取渠道单号ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setStatus(DictionaryResource.PAYOUTSTATUS_55);
			if (payoutmapper.put(p) > 0) {
				GetPayoutChannelOrderNumThread nf = new GetPayoutChannelOrderNumThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 换汇线上获取单号
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void exchange() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Exchange> list = exchangemapper.selectAddlist();
		for (Exchange p : list) {
			log.info("换汇渠道单号ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setStatus(DictionaryResource.PAYOUTSTATUS_55);
			if (exchangemapper.put(p) > 0) {
				GetExchangeChannelOrderNumThread nf = new GetExchangeChannelOrderNumThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 超時3+10分钟未支付订单处理
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void income() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Income> list = incomemapper.selectAddlist();
		for (Income p : list) {
			if (p.getExpireddate().getTime() < new Date().getTime()) {
				TenantIdContext.setTenantId(p.getTenant_id());
				log.info("代收支付超时单号ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
				p.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				incomemapper.put(p);
				//
				Qrcodeaccountorder qao = qrcodeaccountordermapper.getByOrderNum(p.getQrcodeordernum());
				qao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				qrcodeaccountordermapper.put(qao);
				//
				Incomemerchantaccountorder imqao = incomemerchantaccountordermapper
						.getByOrderNum(p.getMerchantorderid());
				imqao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				incomemerchantaccountordermapper.put(imqao);
				// 释放收款码数据
				String key = SystemConstant.CACHE_SYS_QRCODE + p.getQrcodeid() + "" + p.getFewamount();
				RedisUtil.delete(key);
				// 处理账户
				qrcodeaccountservice.cancleTotalincome(qao);
				incomemerchantaccountservice.cancleTotalincome(imqao);
				TenantIdContext.remove();
			}
		}
	}
}
