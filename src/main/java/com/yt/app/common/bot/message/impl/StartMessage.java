package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.bot.message.UpdateMessageService;

/**
 * 实时汇率
 * 
 * @author zj
 *
 */
@Component
public class StartMessage implements UpdateMessageService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("飞兔机器人当前群Tgid:" + update.getMessage().getChatId() + "请給我管理员权限。");
		return sendMessage;
	}
}
