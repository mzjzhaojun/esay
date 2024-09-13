package com.yt.app.common.bot.message.Keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboard {
	public static InlineKeyboardMarkup getInlineKeyboardMarkup() {
		List<List<InlineKeyboardButton>> listinlinekeyboardbuttons = new ArrayList<>();
		List<InlineKeyboardButton> inlinekeyboardbuttons = new ArrayList<>();

		InlineKeyboardButton inlinekeyboardbutton = new InlineKeyboardButton();
		inlinekeyboardbutton.setText("A兑换自己用");
		inlinekeyboardbutton.setCallbackData(CallBackDataResource.flash_exchange_201);
		inlinekeyboardbuttons.add(inlinekeyboardbutton);

		InlineKeyboardButton inlinekeyboardbutton2 = new InlineKeyboardButton();
		inlinekeyboardbutton2.setText("B兑换给朋友");
		inlinekeyboardbutton2.setCallbackData(CallBackDataResource.flash_exchange_202);
		inlinekeyboardbuttons.add(inlinekeyboardbutton2);

		listinlinekeyboardbuttons.add(inlinekeyboardbuttons);

		InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
		markupKeyboard.setKeyboard(listinlinekeyboardbuttons);
		return markupKeyboard;
	}
}
