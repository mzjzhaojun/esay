package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.service.TronmemberService;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.message.Keyboard.ButtonResource;
import com.yt.app.common.bot.message.impl.ContactCustomerServiceMessage;
import com.yt.app.common.bot.message.impl.TrxFlashRentMessage;

import lombok.extern.slf4j.Slf4j;

import com.yt.app.common.bot.message.impl.TronStartMessage;
import com.yt.app.common.bot.message.impl.MemberFlashExchangeMessage;
import com.yt.app.common.bot.message.impl.NotifyMermberMessage;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class TronBot extends TelegramLongPollingBot {
	@Autowired
	private TronStartMessage startmessage;
	@Autowired
	private TrxFlashRentMessage trxflashrentmessage;
	@Autowired
	private MemberFlashExchangeMessage usdtflashexchangemessage;
	@Autowired
	private ContactCustomerServiceMessage contactcustomerservicemessage;
	@Autowired
	private TronmemberService tronmemberservice;
	@Autowired
	private NotifyMermberMessage notifymermbermessage;

	@Override
	public String getBotUsername() {
		return "@flytubot";
	}

	@Override
	public String getBotToken() {
		return "7726126730:AAHV97DxD3yU5jmS02EOVpWwndyUOEHAIQo";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chartId = update.hasMessage() ? update.getMessage().getFrom().getId() : update.hasCallbackQuery() ? update.getCallbackQuery().getFrom().getId() : null;
		log.info("Update: {}", update);
		if (chartId == null) {
			log.info("There isn't object Message or CallbackQuery! Update: {}", update);
			return;
		}
		TenantIdContext.removeFlag();
		Tronmember tronmember = tronmemberservice.getByTgId(chartId);
		if (tronmember == null) {
			tronmember = new Tronmember();
			tronmember.setTgid(chartId);
			tronmember.setName(update.hasMessage() ? update.getMessage().getFrom().getFirstName() : update.hasCallbackQuery() ? update.getCallbackQuery().getFrom().getFirstName() : null);
			tronmemberservice.post(tronmember);
		}
		try {
			if (update.hasMessage() && update.getMessage().hasText()) {
				log.info(update.getMessage().getText());

				if (update.getMessage().getText().equals("/start")) {
					execute(startmessage.getUpdate(update));
				} else if (("⚡购买TRX").equals(update.getMessage().getText())) {
					execute(trxflashrentmessage.getUpdate(update));
				} else if (("✈白资兑换").equals(update.getMessage().getText())) {
					execute(usdtflashexchangemessage.getUpdate(update));
				} else if (("🧑‍🚀联系客服").equals(update.getMessage().getText())) {
					execute(contactcustomerservicemessage.getUpdate(update));
				}
			} else if (update.hasCallbackQuery()) {
				String callbackData = update.getCallbackQuery().getData();
				log.info(callbackData);
				if (callbackData.endsWith(ButtonResource.EXCHANGE)) {
					execute(trxflashrentmessage.excuteExchange(update, tronmember));
				}
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

	public void notifyMermberCancel(Long chatid, String ordernum, Double amnout) {
		try {
			SendMessage sm = notifymermbermessage.getNotifyUpdate(chatid, ordernum, amnout);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void notifyMermberSuccess(Long chatid, String ordernum, Double amnout) {
		try {
			SendMessage sm = notifymermbermessage.getNotifyUpdateSuccess(chatid, ordernum, amnout);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void notifyMermberIncomeSuccess(Long chatid, String ordernum, Double amnout) {
		try {
			SendMessage sm = notifymermbermessage.getNotifyIncomeSuccess(chatid, ordernum, amnout);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
