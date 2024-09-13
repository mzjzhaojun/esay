package com.yt.app.common.bot.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.yt.app.common.bot.message.UpdateService;

@Component
public class StartMessage implements UpdateService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("欢迎使用TRX能量租用,能量兑换综合机器人,请勿使用交易所USDT转入");
		ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
		rkm.setResizeKeyboard(true);
		rkm.setOneTimeKeyboard(true);
		rkm.setSelective(true);
		List<KeyboardRow> listkby = new ArrayList<KeyboardRow>();
		KeyboardButton kbb = new KeyboardButton();
		kbb.setText("⚡能量闪租");
		KeyboardButton kbb1 = new KeyboardButton();
		kbb1.setText("✔TRX闪兑");
		KeyboardButton kbb2 = new KeyboardButton();
		kbb2.setText("🧑‍🚀联系客服");
		KeyboardRow kbr = new KeyboardRow();
		kbr.add(kbb);
		kbr.add(kbb1);
		kbr.add(kbb2);
		listkby.add(kbr);
		rkm.setKeyboard(listkby);
		sendMessage.setReplyMarkup(rkm);
		return sendMessage;
	}
}
