package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.api.v1.mapper.TgmessagegroupMapper;
import com.yt.app.common.bot.message.impl.ExchangeMessage;
import com.yt.app.common.bot.message.impl.M2CMessage;
import com.yt.app.common.bot.message.impl.StartMessage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class MessageBot extends TelegramLongPollingBot {

	@Autowired
	private TgmessagegroupMapper tgmessagegroupmapper;

	@Autowired
	private ExchangeMessage exchangemessage;

	@Autowired
	private M2CMessage m2cmessage;

	@Autowired
	private StartMessage startmessage;

	@Override
	public String getBotUsername() {
		return "飞兔消息";
	}

	@Override
	public String getBotToken() {
		return "7896578980:AAEhDiJk11-kfyQELElassFuIMD-PLXkNAw";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.hasMessage() ? update.getMessage().getChat().getId() : null;
		if (chatid == null) {
			log.info("There isn't object Message or CallbackQuery! Update: {}", update);
			return;
		}
		try {
			Tgmessagegroup tmg = tgmessagegroupmapper.getByTgcGroupId(chatid);
			if (tmg == null) {
				tmg = tgmessagegroupmapper.getByTgmGroupId(chatid);
				if (tmg == null) {
					tmg = new Tgmessagegroup();
					tmg.setTgmid(chatid);
					tmg.setStatus(true);
					tmg.setTggroupname(update.getMessage().getChat().getTitle());
					tgmessagegroupmapper.post(tmg);
					execute(startmessage.getUpdate(update));
				}
			}
			if (update.hasMessage() && update.getMessage().hasText()) {
				if (update.getMessage().getText().equals("uj")) {
					execute(exchangemessage.getUpdate(update));
				} else if (update.getMessage().getText().equals("ua")) {
					execute(exchangemessage.getAliUpdate(update));
				} else if (update.getMessage().getText().equals("p") && update.getMessage().getReplyToMessage() != null) {
					SendMessage smg = m2cmessage.getReplyUpdate(update, tmg);
					if (update.getMessage().getReplyToMessage().hasPhoto()) {
						execute(m2cmessage.getUpdateSendPhoto(update, smg.getChatId()));
					}
					execute(smg);
				}
			} else if (update.hasCallbackQuery()) {
			} else if (update.hasMessage() && update.getMessage().hasPhoto()) {
			} else if (update.hasMessage() && update.getMessage().hasAudio()) {
			} else if (update.hasMessage() && update.getMessage().hasVideo()) {
			} else if (update.hasMessage() && update.getMessage().hasLocation()) {
			} else if (update.hasMessage() && update.getMessage().hasDocument()) {
			} else if (update.hasMessage() && update.getMessage().hasContact()) {
			} else if (update.hasMessage() && update.getMessage().hasVoice()) {
			} else if (update.hasMessage() && update.getMessage().hasAnimation()) {
			}
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
