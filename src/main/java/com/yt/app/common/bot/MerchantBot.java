package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.common.bot.message.impl.ExchangeMessage;
import com.yt.app.common.bot.message.impl.MerchantBalanceMessage;
import com.yt.app.common.bot.message.impl.MerchantOrderMessage;
import com.yt.app.common.bot.message.impl.PinMessage;
import com.yt.app.common.bot.message.impl.StartMessage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class MerchantBot extends TelegramLongPollingBot {

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private ExchangeMessage exchangemessage;

	@Autowired
	private MerchantBalanceMessage merchantbalancemessage;

	@Autowired
	private PinMessage pinmessage;

	@Autowired
	private StartMessage startmessage;

	@Autowired
	private ChannelBot channelbot;

	@Autowired
	private MerchantOrderMessage merchantordermessage;

	@Override
	public String getBotUsername() {
		return "飞兔商户";
	}

	@Override
	public String getBotToken() {
		return "7472319600:AAGY998sxdVqHNiOVdW3OE6mnEj3KczMxto";
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
			Tgmerchantgroup tmg = tgmerchantgroupmapper.getByTgGroupId(chatid);
			if (tmg == null) {
				tmg = new Tgmerchantgroup();
				tmg.setTgid(chatid);
				tmg.setStatus(true);
				tmg.setTggroupname(update.getMessage().getChat().getTitle());
				tgmerchantgroupmapper.post(tmg);
				execute(startmessage.getUpdate(update));
			}
			if (update.hasMessage() && update.getMessage().hasText()) {
				if (update.getMessage().getText().equals("uj")) {
					execute(exchangemessage.getUpdate(update));
				} else if (update.getMessage().getText().equals("ua")) {
					execute(exchangemessage.getAliUpdate(update));
				} else if (update.getMessage().getText().equals("查询余额")) {
					execute(merchantbalancemessage.getUpdate(update, tmg));
				} else if (update.getMessage().getReplyToMessage() != null && update.getMessage().getReplyToMessage().hasPhoto()) {
					SendMessage smg = merchantordermessage.getUpdate(update, tmg);
					if (smg.getChatId() != null) {
						channelbot.execute(merchantordermessage.getUpdateSendPhoto(update, smg.getChatId()));
						channelbot.execute(smg);
						execute(merchantordermessage.getUpdateHandler(update));
					} else {
						execute(merchantordermessage.getUpdateNotFind(update));
					}
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

	public void statisticsMerchant(Merchant m) {
		try {
			SendMessage sm = merchantbalancemessage.getUpdate(m);
			if (sm != null) {
				Message msg = execute(sm);
				execute(pinmessage.getUpdate(msg));
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
