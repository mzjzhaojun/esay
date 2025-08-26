package com.yt.app.common.runtask;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.mapper.CrownagentMapper;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.runnable.SynchronousBettingThread;
import com.yt.app.common.util.FootBallUtil;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("master")
@Component
public class TaskMasterConfig {

	@Autowired
	private SysconfigService sysconfigservice;

	@Autowired
	private CrownagentMapper crownagentmapper;

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	/**
	 * 更新实时汇率
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void getOKXExchange() throws InterruptedException {
		log.info("开始查询汇率》》》》》》》》》》");
		sysconfigservice.initSystemData();
		log.info("结束查询汇率》》》》》》》》》》");
	}

	/**
	 * 查询足球注单
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void SynchronousBetting() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Crownagent> list = crownagentmapper.list(new HashMap<String, Object>());
		for (Crownagent c : list) {
			SynchronousBettingThread sf = new SynchronousBettingThread(c);
			threadpooltaskexecutor.execute(sf);
		}
	}

	/**
	 * 掉线重新登录
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0 0/2 * * * ?")
	public void SynchronousFootballLogin() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Crownagent> list = crownagentmapper.list(new HashMap<String, Object>());
		for (int i = 0; i < list.size(); i++) {
			Crownagent t = list.get(i);
			if (t.getSynchronous() && !t.getStatus()) {
				ResponseEntity<String> str = FootBallUtil.LoginFootBall(t.getDomian(), t.getUsername(), t.getPassword(), t.getVer(), t.getOrigin());
				if (str.getStatusCodeValue() == 200) {
					t.setCookie(str.getHeaders().getFirst("Set-Cookie"));
					JSONObject data = JSONUtil.parseObj(str.getBody());
					if (data.getInt("code") == 102) {
						t.setUid(data.getStr("uid"));
					}
					t.setStatus(true);
				} else {
					t.setStatus(false);
				}
				crownagentmapper.put(t);
			}
		}
	}
}
