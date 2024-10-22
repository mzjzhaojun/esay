package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.bot.message.UpdateMessageService;

/**
 * 实时汇率
 * 
 * @author zj
 *
 */
@Component
public class PinMessage implements UpdateMessageService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());

		return sendMessage;
	}

	public PinChatMessage getUpdate(Message msg) {
		PinChatMessage sendMessage = new PinChatMessage();
		sendMessage.setChatId(msg.getChatId().toString());
		sendMessage.setMessageId(msg.getMessageId());
		return sendMessage;
	}
}
