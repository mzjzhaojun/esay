package com.yt.app.common.runtask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskExchange {

	@Scheduled(cron = "0/5 * * * * ?")
	public void exchange() throws InterruptedException {

	}
}
