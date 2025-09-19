package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.api.v1.mapper.TgmessagegroupMapper;
import com.yt.app.common.base.context.TenantIdContext;
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
	private M2CMessage m2cmessage;

	@Autowired
	private StartMessage startmessage;

	@Override
	public String getBotUsername() {
		return "兔子测试";
	}

	@Override
	public String getBotToken() {
		return "7669560814:AAHLWZi72DwdIDMe72qlKXyYjxfef-ujJPg";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.hasMessage() ? update.getMessage().getChat().getId() : null;
		log.info("Update: {}", update);
		if (chatid == null) {
			log.info("There isn't object Message or CallbackQuery! Update: {}", update);
			return;
		}
		boolean flagec = true;
		try {
			TenantIdContext.removeFlag();
			Tgmessagegroup tmg = tgmessagegroupmapper.getByTgcGroupId(chatid);
			if (tmg == null) {
				tmg = tgmessagegroupmapper.getByTgmGroupId(chatid);
				flagec = false;
				if (tmg == null) {
					tmg = new Tgmessagegroup();
					tmg.setTgmid(chatid);
					tmg.setStatus(true);
					tmg.setTggroupname(update.getMessage().getChat().getTitle());
					tmg.setAdminmangers("@BigTigerThree");
					tmg.setMangers("@BigTigerThree");
					tgmessagegroupmapper.post(tmg);
					execute(startmessage.getUpdate(update));
				}
			}
			if (update.hasMessage()) {
				String msg = update.getMessage().getText();
				String username = update.getMessage().getFrom().getUserName();
				if (msg != null && msg.toLowerCase().equals("c") && update.getMessage().getReplyToMessage() != null) {
					SendMessage smg = m2cmessage.getReplyUpdate(update, tmg);
					if (update.getMessage().getReplyToMessage().hasPhoto()) {
						execute(m2cmessage.getUpdateSendRplPhoto(update, smg.getChatId()));
					}
					execute(smg);
					m2cmessage.getReplyRecordUpdate(update, tmg);
				} else if (update.getMessage().getCaption() != null && update.getMessage().getCaption().toLowerCase().equals("c") && update.getMessage().hasPhoto()) {
					SendMessage smg = m2cmessage.getReplyUpdate(update, tmg);
					if (update.getMessage().hasPhoto()) {
						execute(m2cmessage.getUpdateSendPhoto(update, smg.getChatId()));
					}
					execute(smg);
				} else if (flagec && msg != null && msg.startsWith("+") && update.getMessage().getReplyToMessage() != null) {
					SendMessage smg = m2cmessage.getReplyChannelUpdate(update, tmg);
					if (update.getMessage().getReplyToMessage().hasPhoto()) {
						execute(m2cmessage.getUpdateSendRplPhoto(update, smg.getChatId()));
					}
					execute(smg);
				} else if (!flagec && update.getMessage().getCaption() != null && update.getMessage().getCaption().startsWith("+") && update.getMessage().getReplyToMessage() != null) {
					SendMessage smg = m2cmessage.getReplyRecordSuccessUpdate(update, tmg);
					execute(smg);
				} else if (!flagec && msg != null && msg.toLowerCase().startsWith("ss")) {
					SendMessage smg = m2cmessage.getReplyRecordSuccessListUpdate(update, tmg);
					execute(smg);
				} else if (msg != null && !flagec && msg.startsWith("设置操作人") && tmg.getAdminmangers().indexOf(username) != -1) {
					String str = msg.substring(msg.indexOf("人") + 1);
					tmg.setMangers(str);
					if (tgmessagegroupmapper.put(tmg) > 0)
						sendText(chatid, "操作人：" + str + ",设置成功");
				} else if (msg != null && !flagec && msg.startsWith("清空操作人") && tmg.getAdminmangers().indexOf(username) != -1) {
					tmg.setMangers("No");
					if (tgmessagegroupmapper.put(tmg) > 0)
						sendText(chatid, "操作人清空成功");
				} else if (msg != null && flagec && msg.startsWith("设置操作人") && tmg.getAdminmangers().indexOf(username) != -1) {
					String str = msg.substring(msg.indexOf("人") + 1);
					tmg.setCustomermangers(str);
					if (tgmessagegroupmapper.put(tmg) > 0)
						sendText(chatid, "操作人：" + str + ",设置成功");
				} else if (msg != null && flagec && msg.startsWith("清空操作人") && tmg.getAdminmangers().indexOf(username) != -1) {
					tmg.setCustomermangers("No");
					if (tgmessagegroupmapper.put(tmg) > 0)
						sendText(chatid, "操作人清空成功");
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

	// 发送消息
	public Message sendText(Long who, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
		sm.enableMarkdown(true);
		try {
			Message msg = execute(sm);
			return msg;
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}
}
