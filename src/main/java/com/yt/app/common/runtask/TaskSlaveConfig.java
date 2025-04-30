package com.yt.app.common.runtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.service.SysconfigService;

@Profile("dev")
@Component
public class TaskSlaveConfig {
	@Autowired
	private SysconfigService sysconfigservice;

	/**
	 * 更新实时汇率
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void getOKXExchange() throws InterruptedException {
		sysconfigservice.initSystemData();
	}
}
