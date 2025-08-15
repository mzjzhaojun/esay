package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Betting;
import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.entity.Tgfootballgroup;
import com.yt.app.api.v1.mapper.TgfootballgroupMapper;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.message.impl.ExchangeMessage;
import com.yt.app.common.bot.message.impl.NotifyFootballMessage;
import com.yt.app.common.bot.message.impl.StartMessage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class FootBallBot extends TelegramLongPollingBot {

	@Autowired
	private TgfootballgroupMapper tgfootballgroupmapper;

	@Autowired
	private ExchangeMessage exchangemessage;

	@Autowired
	private NotifyFootballMessage notifyfootballmessage;

	@Autowired
	private StartMessage startmessage;

	@Override
	public String getBotUsername() {
		return " @footballmsgBot";
	}

	@Override
	public String getBotToken() {
		return "8334925725:AAFUFW80UH1iQqux-xUXlmNk-GtTBtKiDtI";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.hasMessage() ? update.getMessage().getChat().getId() : null;
		log.info("Update: {}", update);
		if (chatid == null) {
			log.info("There isn't object Message or CallbackQuery! Update: {}", update);
			return;
		}
		try {
			TenantIdContext.removeFlag();
			Tgfootballgroup tmg = tgfootballgroupmapper.getByTgGroupId(chatid);
			if (tmg == null) {
				tmg = tgfootballgroupmapper.getByTgGroupId(chatid);
				if (tmg == null) {
					tmg = new Tgfootballgroup();
					tmg.setTgid(chatid);
					tmg.setStatus(true);
					tmg.setTggroupname(update.getMessage().getChat().getTitle());
					tgfootballgroupmapper.post(tmg);
					execute(startmessage.getUpdate(update));
				}
			}
			if (update.hasMessage() && update.getMessage().hasText()) {
				if (update.getMessage().getText().toUpperCase().equals("UJ")) {
					execute(exchangemessage.getUpdate(update));
				} else if (update.getMessage().getText().toUpperCase().equals("UA")) {
					execute(exchangemessage.getAliUpdate(update));
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

	public void notifyFootBall(Crownagent ct, Betting bt) {
		try {
			SendMessage sm = notifyfootballmessage.getNotifyUpdate(ct, bt);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
