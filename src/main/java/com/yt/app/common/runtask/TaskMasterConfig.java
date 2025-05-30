package com.yt.app.common.runtask;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TronmemberorderMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.TronBot;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.InComeNotifyThread;
import com.yt.app.common.runnable.PayoutNotifyThread;
import com.yt.app.common.runnable.StatisticsThread;
import com.yt.app.common.runnable.SynChannelBalanceThread;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskMasterConfig {

	@Autowired
	private IncomeMapper incomemapper;

	@Autowired
	private ChannelMapper channelmapper;

	@Autowired
	private PayoutMapper payoutmapper;

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Autowired
	private QrcodeaccountService qrcodeaccountservice;

	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;

	@Autowired
	private AgentaccountService agentaccountservice;

	@Autowired
	private TronmemberorderMapper tronmemberordermapper;

	@Autowired
	private TronBot tronbot;

	/**
	 * 查询代付需要通知
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/5 * * * * ?")
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
	@Scheduled(cron = "0/5 * * * * ?")
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
	 * 同步渠道余额
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void synChannelBalance() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Channel> list = channelmapper.getSynList();
		for (Channel c : list) {
			log.info("同步渠道余额：" + c.getName() + "  支付：" + c.getTodayincomecount());
			SynChannelBalanceThread scbt = new SynChannelBalanceThread(c.getId());
			threadpooltaskexecutor.execute(scbt);
		}
	}

	/**
	 * 更新每日新数据
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "30 59 23 * * ?")
	public void updateTodayValue() throws InterruptedException {
		TenantIdContext.removeFlag();
		String date = DateTimeUtil.getDateTime(new Date(), DateTimeUtil.DEFAULT_DATE_FORMAT);

		StatisticsThread statisticsthread = new StatisticsThread(date);
		threadpooltaskexecutor.execute(statisticsthread);
	}

	/**
	 * 代收超时分钟未支付订单处理
	 */
	@Scheduled(cron = "0/50 * * * * ?")
	public void income() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Income> list = incomemapper.selectAddlist();
		for (Income p : list) {
			if (p.getExpireddate().getTime() < new Date().getTime()) {

				TenantIdContext.setTenantId(p.getTenant_id());
				log.info("代收支付超时单号ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
				p.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				p.setRemark("超時取消代收资金￥：" + p.getAmount());
				incomemapper.put(p);
				qrcodeaccountservice.cancleTotalincome(p);
				// 處理商戶
				incomemerchantaccountservice.cancleTotalincome(p);

				// 释放收款码数据
				String key = SystemConstant.CACHE_SYS_QRCODE + p.getQrcodeid() + "" + p.getFewamount();
				if (RedisUtil.hasKey(key))
					RedisUtil.delete(key);

				// 计算代理
				if (p.getAgentid() != null) {
					// 代理账户
					agentaccountservice.cancleTotalincome(p);
				}

				TenantIdContext.remove();
			}
		}
	}

	/**
	 * 充值超时分钟未支付订单处理
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void tronorder() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Tronmemberorder> list = tronmemberordermapper.selectAddlist();
		for (Tronmemberorder p : list) {
			if (p.getExpireddate().getTime() < new Date().getTime()) {

				TenantIdContext.setTenantId(p.getTenant_id());
				log.info("充值兑换超时单号ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
				p.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				p.setRemark("超时充值兑换￥：" + p.getAmount());
				tronmemberordermapper.put(p);
				NumberUtil.removeExchangeFewAmount(p.getFewamount());
				tronbot.notifyMermberCancel(p.getTgid(), p.getOrdernum(), p.getAmount());
				TenantIdContext.remove();
			}
		}
	}
}
