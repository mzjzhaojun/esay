package com.yt.app.common.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.common.base.context.TenantIdContext;

@Component
public class Cbot extends TelegramLongPollingBot {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private PayconfigService payconfigservice;

	@Autowired
	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;

	@Autowired
	private Mbot mbot;

	@Override
	public String getBotUsername() {
		return "rabbityyds_bot";
	}

	@Override
	public String getBotToken() {
		return "6649299837:AAGhyh19Q9KrDFmvZqopJ5t6kwKLuaANCds";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.getMessage().getChat().getId();
		TenantIdContext.setTenantId(1720395906240614400L);
		String message = update.getMessage().getText();
		Message replymsg = update.getMessage().getReplyToMessage();
		System.out.println("cccccccc" + update.toString());
		if (message != null) {
			Tgchannelgroup tmg = tgchannelgroupmapper.getByTgGroupId(chatid);
			if (tmg == null) {
				tmg = tgchannelgroupmapper.getByTgGroupName(update.getMessage().getChat().getTitle());
				if (tmg != null) {
					tmg.setTgid(chatid);
					tgchannelgroupmapper.put(tmg);
				}
				handlemessage(message, chatid, tmg);
			} else {
				if (replymsg != null) {
					Integer replyid = replymsg.getMessageId();
					Tgmerchantchannelmsg tmcm = tgmerchantchannelmsgmapper.getCidReplyid(chatid.toString(), replyid);
					if (tmcm != null) {
						mbot.sendReplyText(tmcm.getMid(), tmcm.getMreplyid(), message);
						sendReplyText(tmcm.getCid(), tmcm.getCreplyid(), "收到，谢谢你的回复.");
					}
				}
				handlemessage(message, chatid, tmg);
			}
		}
		TenantIdContext.remove();
	}

	private void handlemessage(String message, Long chatid, Tgchannelgroup tmg) {
		if (message.equals("lx")) {// 汇率
			List<Payconfig> list = payconfigservice.getDatas();
			StringBuffer sb = new StringBuffer();
			Integer i = 1;
			for (Payconfig pc : list) {
				sb.append(i + "" + pc.getName() + "，价格:" + pc.getExchange() + "\n");
				i++;
			}
			sendText(chatid, sb.toString());
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
