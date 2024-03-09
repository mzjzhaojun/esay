package com.yt.app.common.runtask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.NotifyTyThread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskExchange {
	@Autowired
	private PayoutMapper payoutmapper;
	@Autowired
	private MerchantMapper merchantmapper;
	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Scheduled(cron = "0/10 * * * * ?")
	public void exchange() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Payout> list = payoutmapper.selectNotifylist();
		for (Payout p : list) {
			log.info("通知ID：" + p.getId() + " 状态：" + p.getStatus());
			NotifyTyThread nf = new NotifyTyThread(payoutmapper, merchantmapper, p.getId());
			threadpooltaskexecutor.execute(nf);
			p.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_65);
			payoutmapper.put(p);
		}
	}
}
