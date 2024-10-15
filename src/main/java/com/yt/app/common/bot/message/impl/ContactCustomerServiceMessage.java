package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.bot.message.UpdateMessageService;

@Component
public class ContactCustomerServiceMessage implements UpdateMessageService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("@Engu6");
		return sendMessage;
	}
}
