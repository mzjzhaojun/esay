package com.yt.app.common.bot.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.yt.app.common.bot.message.UpdateMessageService;

@Component
public class StartMessage implements UpdateMessageService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("欢迎使用TRX兑换综合机器人,请勿使用交易所USDT转入");
		ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
		rkm.setResizeKeyboard(true);
		rkm.setOneTimeKeyboard(false);
		rkm.setSelective(false);
		rkm.setIsPersistent(true);
		List<KeyboardRow> listkby = new ArrayList<KeyboardRow>();
		KeyboardButton kbb = new KeyboardButton();
		kbb.setText("⚡购买TRX");
		KeyboardButton kbb1 = new KeyboardButton();
		kbb1.setText("✈购买飞机会员");
		KeyboardButton kbb3 = new KeyboardButton();
		kbb3.setText("🧑‍🚀联系客服");

		KeyboardRow kbr = new KeyboardRow();
		kbr.add(kbb);
		kbr.add(kbb1);
		kbr.add(kbb3);
		listkby.add(kbr);
		rkm.setKeyboard(listkby);
		sendMessage.setReplyMarkup(rkm);
		return sendMessage;
	}
}
