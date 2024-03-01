package com.yt.app.common.runtask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.common.base.context.TenantIdContext;

@Component
public class TaskExchange {

	@Autowired
	private PayoutMapper mapper;

	
	@Scheduled(cron = "0/20 * * * * ?")
	public void exchange() throws InterruptedException {
		TenantIdContext.setTenantId(1720395906240614400L);
		QueryWrapper<Payout> queryWrapper = new QueryWrapper<Payout>();
		List<Payout> list = mapper.selectList(queryWrapper);
		System.out.println(list.size());
		TenantIdContext.remove();
	}
}
