package com.yt.app.common.runtask;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.api.v1.service.SystemstatisticalreportsService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.ExchangeGetChannelOrderNumThread;
import com.yt.app.common.runnable.PayoutGetChannelOrderNumThread;
import com.yt.app.common.runnable.InComeNotifyThread;
import com.yt.app.common.runnable.PayoutNotifyThread;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("master")
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
	private ChannelService channelservice;

	@Autowired
	private MerchantService merchantservice;

	@Autowired
	private ChannelMapper channelmapper;

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

	@Autowired
	private SystemstatisticalreportsService systemstatisticalreportsservice;

	/**
	 * 更新实时汇率
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void getOKXExchange() throws InterruptedException {
		payconfigservice.initSystemData();
	}

	/**
	 * 更新每日新数据
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "59 59 23 * * ?")
	public void updateTodayValue() throws InterruptedException {
		TenantIdContext.removeFlag();
		// 系统
		systemstatisticalreportsservice.updateDayValue();
		String date = DateTimeUtil.getDateTime(new Date(), DateTimeUtil.DEFAULT_DATE_FORMAT);
		// 商户
		List<Merchant> listm = merchantmapper.list(new HashMap<String, Object>());
		listm.forEach(m -> {
			// 单日数据
			merchantservice.updateDayValue(m, date);
		});
		// 渠道
		List<Channel> listc = channelmapper.list(new HashMap<String, Object>());
		listc.forEach(c -> {
			// 单日数据
			channelservice.updateDayValue(c, date);
		});
		// 飞机商户
		List<Tgmerchantgroup> listtmg = tgmerchantgroupmapper.list(new HashMap<String, Object>());
		listtmg.forEach(mg -> {
			tgmerchantgroupmapper.updatetodayvalue(mg.getId());
		});
		// 飞机渠道
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
			log.info("代付通知ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
			p.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_65);
			if (payoutmapper.put(p) > 0) {
				PayoutNotifyThread nf = new PayoutNotifyThread(p.getId());
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
			log.info("代收通知ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
			p.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_65);
			if (incomemapper.put(p) > 0) {
				InComeNotifyThread nf = new InComeNotifyThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 代付线上下单
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
				PayoutGetChannelOrderNumThread nf = new PayoutGetChannelOrderNumThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 换汇线上下单
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
				ExchangeGetChannelOrderNumThread nf = new ExchangeGetChannelOrderNumThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	/**
	 * 代收超时分钟未支付订单处理
	 */
	@Scheduled(cron = "0/59 * * * * ?")
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
				if (RedisUtil.hasKey(key))
					RedisUtil.delete(key);
				// 处理账户
				qrcodeaccountservice.cancleTotalincome(qao);
				incomemerchantaccountservice.cancleTotalincome(imqao);

				TenantIdContext.remove();
			}
		}
	}
}
