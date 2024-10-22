package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.common.bot.message.impl.ExchangeMessage;
import com.yt.app.common.bot.message.impl.NotifyChannelMessage;
import com.yt.app.common.bot.message.impl.PinMessage;
import com.yt.app.common.bot.message.impl.StartMessage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class ChannelBot extends TelegramLongPollingBot {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private ExchangeMessage exchangemessage;

	@Autowired
	private NotifyChannelMessage notifychannelmessage;

	@Autowired
	private PinMessage pinmessage;

	@Autowired
	private StartMessage startmessage;

	@Override
	public String getBotUsername() {
		return "飞兔通道";
	}

	@Override
	public String getBotToken() {
		return "7126079871:AAFQOkrsh2s3ytDrP4ERtMwpWryV3Zs8jc8";
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.info(update.toString());
		Long chatid = update.hasMessage() ? update.getMessage().getChat().getId() : null;
		log.info("chatid: {}", chatid);
		if (chatid == null) {
			log.info("There isn't object Message or CallbackQuery! Update: {}", update);
			return;
		}
		try {
			Tgchannelgroup tmg = tgchannelgroupmapper.getByTgGroupId(chatid);
			if (tmg == null) {
				tmg = new Tgchannelgroup();
				tmg.setTgid(chatid);
				tmg.setStatus(true);
				tmg.setTggroupname(update.getMessage().getChat().getTitle());
				tgchannelgroupmapper.post(tmg);
				execute(startmessage.getUpdate(update));
			}

			if (update.hasMessage() && update.getMessage().hasText()) {
				if (update.getMessage().getText().equals("z0")) {
					execute(exchangemessage.getUpdate(update));
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

	public void notifyChannel(Channel c) {
		try {
			SendMessage sm = notifychannelmessage.getNotifyUpdate(c);
			if (sm != null) {
				Message msg = execute(sm);
				execute(pinmessage.getUpdate(msg));
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}