package com.yt.app.common.runtask;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.GetExchangeChannelOrderNumThread;
import com.yt.app.common.runnable.GetPayoutChannelOrderNumThread;
import com.yt.app.common.runnable.NotifyTyThread;

@Component
public class TaskConfig {

	private static final Logger logger = LoggerFactory.getLogger(TaskConfig.class);

	@Autowired
	private PayoutMapper payoutmapper;
	@Autowired
	private ExchangeMapper exchangemapper;
	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Scheduled(cron = "0/30 * * * * ?")
	public void notifyPayout() throws InterruptedException {
		TenantIdContext.removeFlag();
		// 查询需要通知的数据
		List<Payout> list = payoutmapper.selectNotifylist();
		for (Payout p : list) {
			logger.info("通知ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_65);
			if (payoutmapper.put(p) > 0) {
				NotifyTyThread nf = new NotifyTyThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void payout() throws InterruptedException {
		TenantIdContext.removeFlag();
		// 查询需要下单的单号
		List<Payout> list = payoutmapper.selectAddlist();
		for (Payout p : list) {
			logger.info("获取渠道单号ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setStatus(DictionaryResource.PAYOUTSTATUS_55);
			if (payoutmapper.put(p) > 0) {
				GetPayoutChannelOrderNumThread nf = new GetPayoutChannelOrderNumThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void exchange() throws InterruptedException {
		TenantIdContext.removeFlag();
		// 查询需要下单的单号
		List<Exchange> list = exchangemapper.selectAddlist();
		for (Exchange p : list) {
			logger.info("获取渠道单号ID：" + p.getId() + " 状态：" + p.getStatus());
			p.setStatus(DictionaryResource.PAYOUTSTATUS_55);
			if (exchangemapper.put(p) > 0) {
				GetExchangeChannelOrderNumThread nf = new GetExchangeChannelOrderNumThread(p.getId());
				threadpooltaskexecutor.execute(nf);
			}
		}
	}
}
