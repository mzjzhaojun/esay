package com.yt.app.common.runnable;

import org.springframework.http.ResponseEntity;

import com.yt.app.api.v1.entity.Betting;
import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.mapper.BettingMapper;
import com.yt.app.api.v1.mapper.CrownagentMapper;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.FootBallBot;
import com.yt.app.common.util.FootBallUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class SynchronousBettingThread implements Runnable {

	private Crownagent crownagent;

	public SynchronousBettingThread(Crownagent pcrownagent) {
		crownagent = pcrownagent;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		CrownagentMapper mapper = BeanContext.getApplicationContext().getBean(CrownagentMapper.class);
		BettingMapper bettingmapper = BeanContext.getApplicationContext().getBean(BettingMapper.class);
		FootBallBot footballbot = BeanContext.getApplicationContext().getBean(FootBallBot.class);
		if (crownagent.getStatus()) {
			ResponseEntity<String> str = FootBallUtil.SendFootBall(crownagent.getDomian(), crownagent.getUid(), crownagent.getSelmaxid(), crownagent.getCookie());
			if (str.getStatusCodeValue() == 200) {
				JSONObject data = JSONUtil.parseObj(str.getBody());
				if (data.getInt("code") == null) {
					// 处理数据
					JSONArray array = data.getJSONArray("wagers");
					String maxid = data.getStr("maxid");
					if (!maxid.equals(crownagent.getSelmaxid())) {
						crownagent.setSelmaxid(maxid);
						mapper.put(crownagent);
					}
					Betting bt = null;
					for (int i = 0; i < array.size(); i++) {
						JSONObject betting = array.getJSONObject(i);
						Betting sbt = bettingmapper.getByTid(betting.getStr("TID"));
						if (sbt == null) {
							bt = new Betting();
							bt.setWagerstype(betting.getStr("WAGERSTYPE"));
							bt.setTid(betting.getStr("TID"));
							bt.setTeamc(betting.getStr("TEAM_C"));
							bt.setTeamh(betting.getStr("TEAM_H"));
							bt.setSite(betting.getStr("SITE"));
							String show = betting.getStr("SHOWTEXT_ORDER_TYPE_IORATIO");
							String mshow = show.substring(show.indexOf("@"), show.indexOf("@") + 27);
							int ind = mshow.indexOf(">") + 1;
							bt.setShowtextordertypeioratio("@" + mshow.substring(ind));
							bt.setShowtextleague(betting.getStr("SHOWTEXT_LEAGUE").replace("<br>", ""));
							bt.setOddftype(betting.getStr("ODDF_TYPE"));
							bt.setNumc(betting.getStr("NUM_C"));
							bt.setNumh(betting.getStr("NUM_H"));
							bt.setGt(betting.getStr("GT"));
							bt.setGold(betting.getStr("GOLD"));
							bt.setOrdertype(betting.getStr("ORDER_TYPE"));
							bt.setTime(betting.getStr("TIME"));
							bt.setDate(betting.getStr("DATE"));
							bt.setName(betting.getStr("NAME0"));
							bt.setOrdercon(betting.getStr("ORDER_CON"));
							String textteam = betting.getStr("SHOWTEXT_TEAM");
							Integer indc = textteam.indexOf("(");
							if (indc != -1) {
								bt.setRemark(textteam.substring(indc, indc + 7));
							} else {
								bt.setRemark("无");
							}
							bettingmapper.post(bt);
							if (crownagent.getChannelid() != null && bt.getGt().equals("足球"))
								footballbot.notifyFootBall(crownagent, bt);
						}
					}
				} else {
					crownagent.setStatus(false);
					mapper.put(crownagent);
				}
			}
		}
	}

}
