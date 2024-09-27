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
		return rkm;
	}
}
