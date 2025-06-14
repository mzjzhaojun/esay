package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.message.impl.ExchangeMessage;
import com.yt.app.common.bot.message.impl.MerchantBalanceMessage;
import com.yt.app.common.bot.message.impl.MerchantGetPhotoMessage;
import com.yt.app.common.bot.message.impl.MerchantIssueMessage;
import com.yt.app.common.bot.message.impl.MerchantOrderMessage;
import com.yt.app.common.bot.message.impl.PinMessage;
import com.yt.app.common.bot.message.impl.StartMessage;
import com.yt.app.common.util.StringUtil;

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
	private MerchantIssueMessage merchantissuemessage;

	@Autowired
	private PinMessage pinmessage;

	@Autowired
	private StartMessage startmessage;

	@Autowired
	private ChannelBot channelbot;

	@Autowired
	private MerchantOrderMessage merchantordermessage;

	@Autowired
	private MerchantGetPhotoMessage merchantgetphotomessage;

	@Override
	public String getBotUsername() {
		return "@FlyRabbitMBot";
	}

	@Override
	public String getBotToken() {
		return "7472319600:AAGY998sxdVqHNiOVdW3OE6mnEj3KczMxto";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.hasMessage() ? update.getMessage().getChat().getId() : null;
		if (chatid == null) {
			log.info("There isn't object Message or CallbackQuery! Update: {}", update);
			return;
		}
		try {
			TenantIdContext.removeFlag();
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
				if (update.getMessage().getText().toUpperCase().equals("UJ")) {
					execute(exchangemessage.getUpdate(update));
				} else if (update.getMessage().getText().toUpperCase().equals("UA")) {
					execute(exchangemessage.getAliUpdate(update));
				} else if (update.getMessage().getText().equals("查询")) {
					execute(merchantbalancemessage.getUpdate(update, tmg));
				} else if (update.getMessage().getText().startsWith("下发")) {
					execute(merchantissuemessage.getUpdate(update, tmg));
				} else if (update.getMessage().getReplyToMessage() != null && update.getMessage().getReplyToMessage().hasPhoto()) {
					SendMessage smg = merchantordermessage.getUpdate(update, tmg);
					if (smg.getChatId() != null) {
						channelbot.execute(merchantordermessage.getUpdateSendPhoto(update, smg.getChatId()));
						channelbot.execute(smg);
						execute(merchantordermessage.getUpdateHandler(update));
					} else {
						if (!StringUtil.isChinese(update.getMessage().getText()))
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

	public void sendOrderResultImg(Long mid, String imgurl) {
		try {
			SendPhoto sm = merchantgetphotomessage.getUpdateSendPhoto(mid, imgurl);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void sendOrderResultSuccess(Long mid, String message) {
		try {
			SendMessage sm = merchantgetphotomessage.getUpdateSendSuccess(mid, message);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void sendOrderResultFail(Long mid, String message) {
		try {
			SendMessage sm = merchantgetphotomessage.getUpdateSendFail(mid, message);
			if (sm != null) {
				execute(sm);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
