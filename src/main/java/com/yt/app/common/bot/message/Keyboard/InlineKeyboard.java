package com.yt.app.common.bot.message.Keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboard {
	public static InlineKeyboardMarkup getInlineKeyboardMarkup() {
		List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
		List<InlineKeyboardButton> buttons1 = new ArrayList<>();
		InlineKeyboardButton ikb = new InlineKeyboardButton();
		ikb.setText("A自己用");
		ikb.setCallbackData("17");
		InlineKeyboardButton ikb3 = new InlineKeyboardButton();
		ikb3.setText("B给朋友");
		ikb3.setCallbackData("13");
		buttons1.add(ikb);
		buttons1.add(ikb3);
		buttons.add(buttons1);

		InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
		markupKeyboard.setKeyboard(buttons);
		return markupKeyboard;
	}
}
