package com.yt.app.common.bot.message.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Sysokx;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.common.bot.message.UpdateMessageService;

/**
 * 实时汇率
 * 
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
		List<Sysokx> list = sysconfigservice.getDataTop();
		StringBuffer sb = new StringBuffer();
		Integer i = 1;
		for (Sysokx pc : list) {
			sb.append(i + "" + pc.getName() + "，价格:" + pc.getPrice() + "\n");
			i++;
		}
		sendMessage.setText("*欧易C2C*  \r\n" + sb.toString());
		sendMessage.enableMarkdown(true);
		return sendMessage;
	}

	public SendMessage getAliUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		List<Sysokx> list = sysconfigservice.getAliPayDataTop();
		StringBuffer sb = new StringBuffer();
		Integer i = 1;
		for (Sysokx pc : list) {
			sb.append(i + "" + pc.getName() + "，价格:" + pc.getPrice() + "\n");
			i++;
		}
		sendMessage.setText("*欧易支付宝*  \r\n" + sb.toString());
		sendMessage.enableMarkdown(true);
		return sendMessage;
	}
}
