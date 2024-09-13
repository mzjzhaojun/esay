package com.yt.app.common.bot.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateService {
	SendMessage getUpdate(Update update);
}
