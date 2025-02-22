package com.yt.app.common.bot.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.common.bot.message.UpdateMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 实时汇率
 * 
 * @author zj
 *
 */
@Component
public class NotifyChannelMessage implements UpdateMessageService {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		return sendMessage;
	}

	public SendMessage getNotifyUpdate(Channel cl) {
		SendMessage sendMessage = new SendMessage();
		Tgchannelgroup tcg = tgchannelgroupmapper.getByChannelId(cl.getId());
		if (tcg != null) {
			sendMessage.setChatId(tcg.getTgid());
			sendMessage.setText("通道:*" + cl.getName() + "*\r\n\r\n编号: *" + cl.getAislecode() + "* 下单出错,请火速处理。\r\n \r\n ==============\r\n \r\n*" + DateTimeUtil.getDateTime() + "*");
			sendMessage.enableMarkdown(true);
		} else {
			return null;
		}
		return sendMessage;
	}
}
