package com.yt.app.common.runtask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.InComeNotifyThread;
import com.yt.app.common.runnable.PayoutNotifyThread;
import com.yt.app.common.runnable.SynChannelBalanceThread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("master")
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
	private SysconfigService payconfigservice;

	/**
	 * 更新实时汇率
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void getOKXExchange() throws InterruptedException {
		payconfigservice.initSystemData();
	}

	/**
	 * 查询代付需要通知
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/11 * * * * ?")
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
	 * 同步渠道余额
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void synChannelBalance() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Channel> list = channelmapper.getSynList();
		for (Channel c : list) {
			log.info("同步渠道余额：" + c.getName() + "  支付：" + c.getTodayincomecount());
			SynChannelBalanceThread scbt = new SynChannelBalanceThread(c.getId());
			threadpooltaskexecutor.execute(scbt);
		}
	}
}
