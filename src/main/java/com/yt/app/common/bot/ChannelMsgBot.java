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
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.ExchangeService;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.common.base.context.TenantIdContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChannelMsgBot extends TelegramLongPollingBot {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;

	@Autowired
	private PayconfigService payconfigservice;

	@Autowired
	private ExchangeService exchangeservice;

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
				handlemessage(update, message, tmg);
			} else {
				// 处理返回消息
				handlemessage(update, message, tmg);
			}
		}
		TenantIdContext.remove();
	}

	private void handlemessage(Update update, String message, Tgchannelgroup tmg) {
		String username = update.getMessage().getFrom().getUserName();
		if (message.equals("#h")) {// 汇率
			List<Payconfig> list = payconfigservice.getDataTop();
			StringBuffer sb = new StringBuffer();
			Integer i = 1;
			for (Payconfig pc : list) {
				sb.append(i + "" + pc.getName() + "，价格:" + pc.getExchange() + "\n");
				i++;
			}
			sendText(tmg.getTgid(), sb.toString());
		} else if (message.equals("#fs")
				&& (username.equals(tmg.getAdminmangers()) || username.equals(tmg.getMangers()))) {// 汇率
			if (update.getMessage().getReplyToMessage() != null) {
				String text = update.getMessage().getReplyToMessage().getText();
				Integer index = text.indexOf("单号：") + 3;
				String ordernum = text.substring(index, index + 16);
				exchangeservice.submit(ordernum);
				Tgmerchantchannelmsg tmccm = tgmerchantchannelmsgmapper.getOrderNum(text.substring(index, index + 16));
				Tgmerchantgroup tmmg = tgmerchantgroupmapper.getByTgGroupId(tmccm.getChatid());
				// 更新
				tmmg.setTodaycountorder(tmmg.getTodaycountorder() + 1);
				tmmg.setCountorder(tmmg.getCountorder() + 1);
				tmmg.setTodayusdcount(tmmg.getTodayusdcount() + tmccm.getUsd());
				tmmg.setTodaycount(tmmg.getTodaycount() + tmccm.getAmount());
				tmmg.setCount(tmmg.getCount() + tmccm.getAmount());
				tgmerchantgroupmapper.put(tmmg);
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
