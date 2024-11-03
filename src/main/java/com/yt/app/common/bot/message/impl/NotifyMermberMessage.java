package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.bot.message.UpdateMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 实时汇率
 * 
 * @author zj
 *
 */
@Component
public class NotifyMermberMessage implements UpdateMessageService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());

		return sendMessage;
	}

	public SendMessage getNotifyUpdate(Long chatid, String ordernum, Double amount) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatid);
		sendMessage.setText("订单:*" + ordernum + "*\r\n\r\n:(*" + amount + "U*) 兑换已超时，请重新下单。\r\n \r\n ==============\r\n \r\n*" + DateTimeUtil.getDateTime() + "*");
		sendMessage.enableMarkdown(true);
		return sendMessage;
	}

	public SendMessage getNotifyUpdateSuccess(Long chatid, String ordernum, Double amount) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatid);
		sendMessage.setText("订单:*" + ordernum + "*\r\n\r\n: (*" + amount + "*) 兑换成功。\r\n \r\n ==============\r\n \r\n*" + DateTimeUtil.getDateTime() + "*");
		sendMessage.enableMarkdown(true);
		return sendMessage;
	}
}
