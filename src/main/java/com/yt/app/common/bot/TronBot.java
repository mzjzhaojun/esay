package com.yt.app.common.bot;

import java.io.File;

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

import com.yt.app.common.bot.message.impl.ContactCustomerServiceMessage;
import com.yt.app.common.bot.message.impl.EnergyFlashRentMessage;
import com.yt.app.common.bot.message.impl.StartMessage;
import com.yt.app.common.bot.message.impl.USDTFlashExchangeMessage;

@Component
public class TronBot extends TelegramLongPollingBot {
	@Autowired
	private StartMessage startmessage;
	@Autowired
	private EnergyFlashRentMessage energyflashrentmessage;
	@Autowired
	private USDTFlashExchangeMessage usdtflashexchangemessage;
	@Autowired
	private ContactCustomerServiceMessage contactcustomerservicemessage;

	@Override
	public String getBotUsername() {
		return "Trx闪兑";
	}

	@Override
	public String getBotToken() {
		return "7507442795:AAHGhestAKLkoKaD-KAF6DBUkjV686_5H6A";
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			System.out.println(update.getMessage().getText());
			try {
				if (update.getMessage().getText().equals("/start")) {
					execute(startmessage.getUpdate(update));
				} else if (("⚡能量闪租").equals(update.getMessage().getText())) {
					execute(energyflashrentmessage.getUpdate(update));
				} else if (("✔TRX闪兑").equals(update.getMessage().getText())) {
					execute(usdtflashexchangemessage.getUpdate(update));
				} else if (("🧑‍🚀联系客服").equals(update.getMessage().getText())) {
					execute(contactcustomerservicemessage.getUpdate(update));
				}
			} catch (TelegramApiException e) {
				throw new RuntimeException(e);
			}
		} else if (update.hasCallbackQuery()) {

		} else if (update.hasMessage() && update.getMessage().hasPhoto()) {
			// todo 处理图片输入
		} else if (update.hasMessage() && update.getMessage().hasAudio()) {
			// todo 处理音频输入
		} else if (update.hasMessage() && update.getMessage().hasVideo()) {
			// todo 处理视频输入
		} else if (update.hasMessage() && update.getMessage().hasLocation()) {
			// todo 处理位置输入
		} else if (update.hasMessage() && update.getMessage().hasDocument()) {
			// todo 处理文件输入
		} else if (update.hasMessage() && update.getMessage().hasContact()) {
			// todo 处理联系人输入
		} else if (update.hasMessage() && update.getMessage().hasVoice()) {
			// todo 处理音频文件输入
		} else if (update.hasMessage() && update.getMessage().hasAnimation()) {
			// todo 处理动画输入
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
