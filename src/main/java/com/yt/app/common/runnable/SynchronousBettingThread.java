package com.yt.app.common.runnable;

import org.springframework.http.ResponseEntity;

import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.common.util.FootBallUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SynchronousBettingThread implements Runnable {

	private Crownagent crownagent;

	public SynchronousBettingThread(Crownagent pcrownagent) {
		crownagent = pcrownagent;
	}

	@Override
	public void run() {
//		ResponseEntity<String> str = FootBallUtil.SendFootBall("https://ag.hga027.com/transform.php?ver=version-08-07", crownagent.getUid(), crownagent.getSelmaxid(), crownagent.getCookie());
//		log.info(str.getBody());
	}

}
