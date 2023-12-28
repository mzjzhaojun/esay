package com.yt.app.common.runtask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskExchange {

	@Scheduled(cron = "0/30 * * * * ?")
	public void exchange() {
		System.out.println("30秒执行");
	}
}
