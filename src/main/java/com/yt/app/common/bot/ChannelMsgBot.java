package com.yt.app.common.bot;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChannelMsgBot extends TelegramLongPollingBot {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private PayconfigService payconfigservice;

	@Autowired
	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;

	@Autowired
	private MerchantMsgBot mbot;

	@Override
	public String getBotUsername() {
		return "飞兔运营";
	}

	@Override
	public String getBotToken() {
		return "7126079871:AAFQOkrsh2s3ytDrP4ERtMwpWryV3Zs8jc8";
	}

	@Override
	public void onUpdateReceived(Update update) {

		log.info(update.toString());

		Long chatid = update.getMessage().getChat().getId();
		String message = update.getMessage().getText();
		Message replymsg = update.getMessage().getReplyToMessage();

		if (message != null) {
			TenantIdContext.removeFlag();
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
				handlemessage(message, tmg);
			} else {
				if (replymsg != null) {
					Integer replyid = replymsg.getMessageId();
					Tgmerchantchannelmsg tmcm = tgmerchantchannelmsgmapper.getCidReplyid(chatid.toString(), replyid);
					if (tmcm != null) {
						mbot.sendReplyText(tmcm.getMid(), tmcm.getMreplyid(), message);
						sendReplyText(tmcm.getCid(), tmcm.getCreplyid(), "收到，谢谢你的回复.");
					}
				}
				handlemessage(message, tmg);
			}
		}
		TenantIdContext.remove();
	}

	private void handlemessage(String message, Tgchannelgroup tmg) {
		if (message.equals("#h")) {// 汇率
			List<Payconfig> list = payconfigservice.getDataTop();
			StringBuffer sb = new StringBuffer();
			Integer i = 1;
			for (Payconfig pc : list) {
				sb.append(i + "" + pc.getName() + "，价格:" + pc.getExchange() + "\n");
				i++;
			}
			sendText(tmg.getTgid(), sb.toString());
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

	// 发送照片
	public Message sendPhoto(Long who, String what) {
		SendPhoto sm = SendPhoto.builder().chatId(who.toString()).photo(new InputFile(new File(what))).build();
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
