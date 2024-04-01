package com.yt.app.common.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.common.base.context.BeanContext;

public class Messagebot extends TelegramLongPollingBot {

	private TgchannelgroupMapper tgchannelgroupmapper;

	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;

	private String name;
	private String token;

	public Messagebot() {
	}

	public Messagebot(String _name, String _token) {
		tgchannelgroupmapper = BeanContext.getApplicationContext().getBean(TgchannelgroupMapper.class);
		tgmerchantchannelmsgmapper = BeanContext.getApplicationContext().getBean(TgmerchantchannelmsgMapper.class);
		name = _name;
		token = _token;
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.getMessage().getChat().getId();
		String message = update.getMessage().getText();
		Message replymsg = update.getMessage().getReplyToMessage();
		System.out.println("message:" + update.toString());
		if (message != null) {
			Tgchannelgroup tmg = tgchannelgroupmapper.getByTgGroupId(chatid);
			if (tmg == null) {
				tmg = tgchannelgroupmapper.getByTgGroupName(update.getMessage().getChat().getTitle());
				if (tmg != null) {
					tmg.setTgid(chatid);
					tgchannelgroupmapper.put(tmg);
				} else {
					Tgchannelgroup t = new Tgchannelgroup();
					t.setTgid(chatid);
					t.setStatus(true);
					t.setTggroupname(update.getMessage().getChat().getTitle());
					tgchannelgroupmapper.post(t);
				}

			} else {
				if (replymsg != null) {
					Integer replyid = replymsg.getMessageId();
					Tgmerchantchannelmsg tmcm = tgmerchantchannelmsgmapper.getCidReplyid(chatid.toString(), replyid);
					if (tmcm != null) {

						sendReplyText(tmcm.getCid(), tmcm.getCreplyid(), "收到，谢谢你的回复.");
					}
				}

			}
		}
	}

	// 发送消息
	public Message sendText(Long who, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
		try {
			Message msg = execute(sm);
			return msg;
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 发送回复消息
	public void sendReplyText(Long who, Integer replyid, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).replyToMessageId(replyid).build();
		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 复制消息
	public void copyMessage(Long who, Integer msgId) {
		CopyMessage cm = CopyMessage.builder().fromChatId(who.toString()).chatId(who.toString()).messageId(msgId)
				.build();
		try {
			execute(cm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 转发
	public void forwardMessage(Long who, Integer msgId) {
		ForwardMessage cm = ForwardMessage.builder().fromChatId(who.toString()).chatId(who.toString()).messageId(msgId)
				.build();
		try {
			execute(cm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).parseMode("HTML").text(txt).replyMarkup(kb)
				.build();

		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
