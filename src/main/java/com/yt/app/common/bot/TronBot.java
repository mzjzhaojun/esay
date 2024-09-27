package com.yt.app.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.common.bot.message.Keyboard.ButtonResource;
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
		try {
			if (update.hasMessage() && update.getMessage().hasText()) {
				System.out.println(update.getMessage().getText());

				if (update.getMessage().getText().equals("/start")) {
					execute(startmessage.getUpdate(update));
				} else if (("⚡能量闪租").equals(update.getMessage().getText())) {
					execute(energyflashrentmessage.getUpdate(update));
				} else if (("✔TRX闪兑").equals(update.getMessage().getText())) {
					execute(usdtflashexchangemessage.getUpdate(update));
				} else if (("🧑‍🚀联系客服").equals(update.getMessage().getText())) {
					execute(contactcustomerservicemessage.getUpdate(update));
				}
			} else if (update.hasCallbackQuery()) {

				String callbackData = update.getCallbackQuery().getData();
				if (ButtonResource.flash_exchange_201.getCallBackData().equals(callbackData)) {
					execute(usdtflashexchangemessage.excuteExchange(update));
				}

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
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}
}
