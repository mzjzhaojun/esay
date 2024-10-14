package com.yt.app.common.bot.message.Keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardMarkup {
	public static ReplyKeyboardMarkup getReplyKeyboardMarkup() {
		ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
		rkm.setResizeKeyboard(true);
		rkm.setOneTimeKeyboard(false);
		rkm.setSelective(false);
		rkm.setIsPersistent(true);
		List<KeyboardRow> listkby = new ArrayList<KeyboardRow>();
		KeyboardButton kbb = new KeyboardButton();
		kbb.setText("⚡TRX闪兑");
		KeyboardButton kbb1 = new KeyboardButton();
		kbb1.setText("✈飞机账号");
		KeyboardButton kbb3 = new KeyboardButton();
		kbb3.setText("🧑‍🚀联系客服");
		KeyboardRow kbr = new KeyboardRow();
		kbr.add(kbb);
		kbr.add(kbb1);
		kbr.add(kbb3);
		listkby.add(kbr);

		rkm.setKeyboard(listkby);
		return rkm;
	}
}
