package com.yt.app.common.bot.message.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.common.bot.message.UpdateMessageService;

/**
 * 实时汇率
 * @author zj
 *
 */
@Component
public class ExchangeMessage implements UpdateMessageService {

	@Autowired
	private SysconfigService sysconfigservice;

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		List<Sysconfig> list = sysconfigservice.getDataTop();
		StringBuffer sb = new StringBuffer();
		Integer i = 1;
		for (Sysconfig pc : list) {
			sb.append(i + "" + pc.getName() + "，价格:" + pc.getExchange() + "\n");
			i++;
		}
		sendMessage.setText(sb.toString());
		return sendMessage;
	}
}
