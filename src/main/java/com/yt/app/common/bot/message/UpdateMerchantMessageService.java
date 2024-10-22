package com.yt.app.common.bot.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Tgmerchantgroup;

public interface UpdateMerchantMessageService {

	SendMessage getUpdate(Update update, Tgmerchantgroup tmg);
}
