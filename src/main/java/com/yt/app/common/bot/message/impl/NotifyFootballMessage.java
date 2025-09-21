package com.yt.app.common.bot.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Betting;
import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.entity.Tgfootballgroup;
import com.yt.app.api.v1.mapper.TgfootballgroupMapper;
import com.yt.app.common.bot.message.UpdateMessageService;

/**
 * 实时汇率
 * 
 * @author zj
 *
 */
@Component
public class NotifyFootballMessage implements UpdateMessageService {

	@Autowired
	private TgfootballgroupMapper tgfootballgroupmapper;

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		return sendMessage;
	}

	public SendMessage getNotifyUpdate(Crownagent ct, Betting bt) {
		SendMessage sendMessage = new SendMessage();
		Tgfootballgroup tcg = tgfootballgroupmapper.getByTgGroupId(ct.getChannelid());
		if (tcg != null) {
			sendMessage.setChatId(tcg.getTgid());
			sendMessage.setText("代理:*" + ct.getName() + "*\r\n会员:*" + bt.getName() + "*\r\n联赛: *" + bt.getShowtextleague() + "*\r\n盘口: *" + bt.getOddftype() + "*\r\n类型: *" + bt.getGt() + "*\r\n规则: *" + bt.getWagerstype() + "*\r\n比分: *"
					+ bt.getRemark() + "*\r\n买盘: *" + bt.getOrdertype() + " " + bt.getOrdercon() + "*\r\n对阵: *" + bt.getTeamh().trim() + " VS " + bt.getTeamc().trim() + "*\r\n水位: *" + bt.getShowtextordertypeioratio() + "*\r\n注额: *" + bt.getGold() + "*\r\n日期: *"
					+ bt.getTime() + " " + bt.getDate() + "*");
			sendMessage.enableMarkdown(true);
		} else {
			return null;
		}
		return sendMessage;
	}
}
